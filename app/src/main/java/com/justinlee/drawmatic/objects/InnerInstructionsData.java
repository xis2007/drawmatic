package com.justinlee.drawmatic.objects;

public class InnerInstructionsData {
    private String mInstructionTitle;
    private String mInstructionContent;
    private String mImageSource;

    public InnerInstructionsData(String instructionTitle, String instructionContent, String imageSource) {
        mInstructionTitle = instructionTitle;
        mInstructionContent = instructionContent;
        mImageSource = imageSource;
    }

    public String getInstructionTitle() {
        return mInstructionTitle;
    }

    public void setInstructionTitle(String instructionTitle) {
        mInstructionTitle = instructionTitle;
    }

    public String getInstructionContent() {
        return mInstructionContent;
    }

    public void setInstructionContent(String instructionContent) {
        mInstructionContent = instructionContent;
    }

    public String getImageSource() {
        return mImageSource;
    }

    public void setImageSource(String imageSource) {
        mImageSource = imageSource;
    }
}
