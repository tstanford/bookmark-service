package com.timstanford.bookmarkservice.api;

public class GroupMoveRequest {
    private int sourceGroupId;
    private int targetGroupId;

    public int getSourceGroupId() {
        return sourceGroupId;
    }

    public void setSourceGroupId(int sourceGroupId) {
        this.sourceGroupId = sourceGroupId;
    }

    public int getTargetGroupId() {
        return targetGroupId;
    }

    public void setTargetGroupId(int targetGroupId) {
        this.targetGroupId = targetGroupId;
    }
}
