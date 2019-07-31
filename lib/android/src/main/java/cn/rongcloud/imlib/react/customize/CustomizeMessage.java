package cn.rongcloud.imlib.react.customize;

import android.os.Parcel;
import android.util.Log;

import com.facebook.react.bridge.ReadableMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import io.rong.common.ParcelUtils;
import io.rong.imlib.DestructionTag;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

@MessageTag(value = "WL:Message", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class CustomizeMessage extends MessageContent {

  @Override
  public String toString() {
    return "CustomizeMessage{" +
        "content=" + content +
        '}';
  }

  private String content;//消息属性，可随意定义

  public String getContent() {
    return content;
  }

  public static CustomizeMessage obtain(String content) {
    CustomizeMessage obj = new CustomizeMessage(content);
    return obj;
  }

  public CustomizeMessage(String content) {
    this.content = content;
  }

  /**
   * 对收到的消息进行解析，先由 byte 转成 json 字符串，再将 json
   * "封装"查看encode()
   *
   * @param data
   */
  public CustomizeMessage(byte[] data) {
    String jsonStr = null;

    try {
      jsonStr = new String(data, "UTF-8");
    } catch (UnsupportedEncodingException e1) {

    }

    try {
      JSONObject jsonObj = new JSONObject(jsonStr);

      if (jsonObj.has("content"))
        content = jsonObj.getString("content");

    } catch (JSONException e) {
      Log.e("JSONException", e.getMessage());
    }

  }


  /**
   * 将消息属性封装成 json 串，再将 json 串转成 byte 数组，该方法会在发消息时调用
   * "解析"查看CustomizeMessage(byte[] data)
   *
   * @return byte[]
   */
  @Override
  public byte[] encode() {
    JSONObject jsonObj = new JSONObject();

    try {
      jsonObj.put("content", content);
    } catch (JSONException e) {
      Log.e("JSONException", e.getMessage());
    }
    try {
      return jsonObj.toString().getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    return null;
  }

  /////////////////////////////////////////////
  /**
   * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
   */
  public static final Creator<CustomizeMessage> CREATOR = new Creator<CustomizeMessage>() {

    @Override
    public CustomizeMessage createFromParcel(Parcel source) {
      return new CustomizeMessage(source);
    }

    @Override
    public CustomizeMessage[] newArray(int size) {
      return new CustomizeMessage[size];
    }
  };

  /**
   * 给消息赋值。
   *
   * @param in
   */
  public CustomizeMessage(Parcel in) {
    content = ParcelUtils.readFromParcel(in);//该类为工具类，消息属性
    //这里可继续增加你消息的属性
  }

  /**
   * 将类的数据写入外部提供的 Parcel 中。
   *
   * @param dest  对象被写入的 Parcel。
   * @param flags 对象如何被写入的附加标志。
   */
  @Override
  public void writeToParcel(Parcel dest, int flags) {
    ParcelUtils.writeToParcel(dest, content);//该类为工具类，对消息中属性进行序列化
  }


  /////////////////////////////////////////////
  @Override
  public int describeContents() {
    return 0;
  }
}
