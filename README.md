# FastJson2



## 基于fastjson2，基于fastjson2通过字符串查找对应的json对象或者json数组

### 前往fastjson2：https://github.com/alibaba/fastjson2/tree/main



### java代码

```java
package top.generate;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

/**
 * @author pwx
 * @version 1.0
 * @createDate: 2022年06月19
 * @comment 字符查找内容
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
                        return get(o, index.substring(parenthesisRight + 2));
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

```



## 使用教程

### json对象的使用

```json
{
    "name":"李白",
    "age":90,
    "number":[
        1,
        2,
        3,
        4,
        5,
        {
            "like":"2"
        }
    ]
}
```





```java
package com.test;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import top.generate.Json;

public class FastJson2Test {

    public static void main(String[] args) {
        String json = "{\"name\":\"李白\",\"age\":90,\"number\":[1,2,3,4,5,{\"like\":\"2\"}]}";
        JSONObject jsonObject = JSON.parseObject(json);

        System.out.println(Json.get(jsonObject, "name"));		//查找姓名
        System.out.println(Json.get(jsonObject, "age"));		//查找年龄
        System.out.println(Json.get(jsonObject, "number[5]"));	//查找数据
        System.out.println(Json.get(jsonObject, "number[5].like"));//查找李白喜欢的数字
        System.out.println(Json.get(jsonObject, "email"));		//查找不存在的数据
    }
}
```



### 结果

```
李白
90
{"like":"2"}
2
null
```



------



## json数组查找

### json数据

```
[
    {
        "name":"刘备",
        "age":23
    },
    {
        "name":"关羽",
        "age":24,
        "tool":[
            "🔪",
            {
                "name":"🐎"
            },
            "🚗"
        ]
    },
    {
        "name":"张飞",
        "age":32
    }
]

```



### 使用方法

```
package com.test;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import top.generate.Json;

public class FastJson2Test {

    public static void main(String[] args) {
        String json = "[{\"name\":\"刘备\",\"age\":23},{\"name\":\"关羽\",\"age\":24,\"tool\":[\"\uD83D\uDD2A\",{\"name\":\"\uD83D\uDC0E\"},\"\uD83D\uDE97\"]},{\"name\":\"张飞\",\"age\":32}]";
        JSONArray jsonArray = JSON.parseArray(json);
        
        System.out.println(Json.get(jsonArray, "[2]"));             //查找张飞的数据
        System.out.println(Json.get(jsonArray, "[1].name"));        //查找关羽的姓名
        System.out.println(Json.get(jsonArray, "[1].tool"));        //查找关羽的所有工具
        System.out.println(Json.get(jsonArray, "[1].tool[2]"));     //查找关羽的车
        System.out.println(Json.get(jsonArray, "[1].tool[1].name"));//查找关羽的宝马
    }
}

```



### 结果

```
{"name":"张飞","age":32}
关羽
["🔪",{"name":"🐎"},"🚗"]
🚗
🐎
```

