package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName QuestionListBean
 * @Date 2018/10/26 15:55
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class QuestionListBean {


    /**
     * hasTodayWeight : true
     * optionList : [{"optionDesc":"string","optionNo":"string"}]
     * question : string
     * questionOption : string
     * sort : 0
     */

    private boolean hasTodayWeight;
    private String question;
    private String questionOption;
    private int sort;
    private String imgUrl;
    private List<OptionListBean> optionList;


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isHasTodayWeight() {
        return hasTodayWeight;
    }

    public void setHasTodayWeight(boolean hasTodayWeight) {
        this.hasTodayWeight = hasTodayWeight;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionOption() {
        return questionOption;
    }

    public void setQuestionOption(String questionOption) {
        this.questionOption = questionOption;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<OptionListBean> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<OptionListBean> optionList) {
        this.optionList = optionList;
    }

    public static class OptionListBean {
        /**
         * optionDesc : string
         * optionNo : string
         */

        private String optionDesc;
        private String optionNo;
        private boolean isSelect;


        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getOptionDesc() {
            return optionDesc;
        }

        public void setOptionDesc(String optionDesc) {
            this.optionDesc = optionDesc;
        }

        public String getOptionNo() {
            return optionNo;
        }

        public void setOptionNo(String optionNo) {
            this.optionNo = optionNo;
        }
    }
}
