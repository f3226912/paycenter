package cn.gdeng.paycenter.admin.dto.admin;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description easyUI 树节点
 * @Project gd-admin-intf
 * @ClassName TreeNode.java
 * @Author lidong(dli@gdeng.cn)
 * @CreationDate 2016年1月14日 上午8:28:32
 * @Version V2.0
 * @Copyright 谷登科技 2015-2015
 * @ModificationHistory
 */
public class TreeNode implements Serializable {

    private static final long serialVersionUID = 7653473078405232220L;

    private String id;
    private String text;
    private String state;// 节点状态，'open' 或 'closed'，默认：'open'。如果为'closed'的时候，将不自动展开该节点。
    private String checked;// 表示该节点是否被选中。true/false

    private Map<String, Object> attributes;// 被添加到节点的自定义属性。
    private List<TreeNode> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }
}
