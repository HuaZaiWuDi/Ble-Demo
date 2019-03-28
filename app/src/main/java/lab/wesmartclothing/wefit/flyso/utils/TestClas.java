package lab.wesmartclothing.wefit.flyso.utils;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @Package lab.wesmartclothing.wefit.flyso.utils
 * @FileName TestClas
 * @Date 2018/12/13 15:54
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class TestClas {


    public static void main(String[] arrs) {

        String jsonStr = "[{\"age\":0,\"sex\":0,\"weight\":0}]";

        List<TestJson> fromJson = JSON.parseObject(jsonStr, new TypeToken<List<TestJson>>() {
        }.getType());
//        TestJson fromJson = new Gson().fromJson(jsonStr, TestJson.class);
        System.out.print("age:" + fromJson.size());
    }




    static class TestJson {


        /**
         * age : 0
         * sex : 0
         * weight : 0
         */

        private int age;
        private int sex;
        private int weight;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }


}
