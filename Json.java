package top.generate;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

/**
 * @author pwx
 * @version 1.0
 * @createDate: 2022年06月19
 * @comment 用于解析文本读取字符内容
 */
public class Json {
    /**
     *
     * 用于解析jsonfast2文本内容
     * @param object json对象
     * @param index 查找内容
     * @return obj
     */
    public static Object get(Object object, String index){
        //从对象中查找
        Object obj = null;
        if (object instanceof JSONObject){
            JSONObject jsonObject = (JSONObject) object;
            int parenthesisLeft = index.indexOf("[");
            if (parenthesisLeft != -1){//如何index是 name[0]类型则,需要匹配
                String subParenthesisLeft = index.substring(0, parenthesisLeft);//截取左边括号的内容
                return get(jsonObject.get(subParenthesisLeft), index.substring(parenthesisLeft));
            }else {//如果 index.name或者index 是 name 类型
                int spot = index.indexOf(".");//是否包含点
                if (spot != -1) {//包含点后面后面还有需要去读取的内容
                    return get(jsonObject.get(index.substring(0, spot)), index.substring(spot + 1));//返回json
                }else {//当没有数组内容的时候
                    return jsonObject.get(index);
                }
            }
        }

        //从数组中查找
        if (object instanceof JSONArray){
            JSONArray jsonArray = (JSONArray) object;
            int parenthesisLeft = index.indexOf("[");
            if (parenthesisLeft != -1){//数组类型判断是否有括号
                int parenthesisRight = index.indexOf("]");//截取右边的括号
                int spot = index.indexOf(".");//是否包含点
                if (spot != -1){//说明包含点
                    String middleBracket = index.substring(parenthesisLeft + 1, parenthesisRight);//截取中间数组内容
                    Object o= jsonArray.get(new Integer(middleBracket));
                    if (parenthesisLeft == index.lastIndexOf("[")){//判断最后一个[ 括号和左边 [ 的位置相同
                        return get(o, index.substring(spot + 1));
                    }else {
                        return get(o, index.substring(parenthesisRight + 1));
                    }
                }else {
                    //如果不包含点则对周到[0]括号中间的数字
                    String middleBracket = index.substring(parenthesisLeft + 1, parenthesisRight);
                    return jsonArray.get(new Integer(middleBracket));
                }
            }
        }
        return obj;
    }
}
