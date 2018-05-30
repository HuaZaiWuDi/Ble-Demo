package lab.wesmartclothing.wefit.flyso.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JSONUtil {
    public static <T> String fromList(List<T> list) throws Throwable {
        StringBuilder json = new StringBuilder();

        if (list == null || list.size() == 0) {
            return "";
        }

        json.append("[");

        for (int i = 0; i < list.size(); i++) {
            json.append("{");
            T t = list.get(i);
            Class clazz = t.getClass();
            Field[] fields = t.getClass().getFields();
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                String strFields = field.getName();
                String getMethodName = "get" + strFields.substring(0, 1).toUpperCase() + strFields.substring(1);
                Method method = clazz.getMethod(getMethodName, new Class[]{});
                Object value = method.invoke(t, new Object[]{});
                json.append("\"" + strFields + "\"" + ":" + "\"" + value + "\"");

                if (j < fields.length - 1) {
                    json.append(",");
                }
            }
            json.append("}");
            if (i < list.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        return json.toString();
    }



}  