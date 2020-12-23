package xlk.takstar.paperless.model.bean;

import com.mogujie.tt.protobuf.InterfaceIM;

/**
 * @author xlk
 * @date 2020/3/17
 * @desc 会议交流的自定义信息
 */
public class ChatMessageA {
    /**
     * =0收到的信息，=1自己发送的信息
     */
    private int type;
    private String name;
    private InterfaceIM.pbui_Item_MeetIMDetailInfo message;

    public ChatMessageA(int type, String name, InterfaceIM.pbui_Item_MeetIMDetailInfo message) {
        this.type = type;
        this.name = name;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InterfaceIM.pbui_Item_MeetIMDetailInfo getMessage() {
        return message;
    }

    public void setMessage(InterfaceIM.pbui_Item_MeetIMDetailInfo message) {
        this.message = message;
    }
}
