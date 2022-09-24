package com.vk.camerapreviewexample.view.activity;

public class DotsPointArray {
    final float[] bufferArray;
    final int bufferSize, numValuesPerPoint,numPointsPerElement,numElements;
    int currPos;

    DotsPointArray(int numElements,int numPointsPerElement){
        this(numElements,numPointsPerElement,2);
    }

    public DotsPointArray(int numElements, int numPointsPerElement, int numValuesPerPoint) {
        this.currPos=0;
        this.numElements = numElements;
        this.numValuesPerPoint = numValuesPerPoint;
        this.numPointsPerElement = numPointsPerElement;
        this.bufferSize=numElements*numPointsPerElement*numValuesPerPoint;
        this.bufferArray=new float[this.bufferSize];
    }

    public boolean add(float... args){
        int numInputVal=args.length;
        if (numInputVal!=numPointsPerElement*numValuesPerPoint){
            return false;
        }
        for (int i=0;i<numInputVal;i++){
            bufferArray[(currPos+i)%bufferSize]=args[i];
        }
        currPos=(currPos+numInputVal)%bufferSize;
        return true;
    }

    public float[] getArray(){
        return this.getIndexedArray(0);
    }

    public float[] getIndexedArray(int startindx) {
        float[] opbuffr=new float[this.bufferSize];
        int actualPosition=0;
        for (int i=0;i<numElements;i++){
            for (int j=0;j<numPointsPerElement;j++){
                actualPosition=i*numPointsPerElement*numValuesPerPoint+j*numPointsPerElement;
                opbuffr[actualPosition]=i+startindx;
                for (int k=1;k<numValuesPerPoint;k++){
                    actualPosition++;
                    opbuffr[actualPosition]=bufferArray[(currPos+actualPosition)%bufferSize];

                }
            }
        }
        return opbuffr;
    }
}
