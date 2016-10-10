package com.example.youngkaaa.multirecycleradapter;

/**
 * Created by : youngkaaa on 2016/10/9.
 * Contact me : 645326280@qq.com
 */

public class TitleBean {
    private String leftTitle;
    private String rightTitle;

    public TitleBean() {
    }

    public TitleBean(String left, String right) {
        this.leftTitle = left;
        this.rightTitle = right;
    }

    public void setLeftTitle(String leftTitle) {
        this.leftTitle = leftTitle;
    }

    public String getLeftTitle() {
        return leftTitle;
    }

    public void setRightTitle(String rightTitle) {
        this.rightTitle = rightTitle;
    }

    public String getRightTitle() {
        return rightTitle;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        TitleBean person = (TitleBean) obj;

        if (leftTitle != null && person.leftTitle != null && rightTitle != null && person.rightTitle != null
                && leftTitle.equals(person.leftTitle) && person.rightTitle.equals(rightTitle)) {
            return true;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return 7 * leftTitle.hashCode() + 13 * rightTitle.hashCode();
    }
}
