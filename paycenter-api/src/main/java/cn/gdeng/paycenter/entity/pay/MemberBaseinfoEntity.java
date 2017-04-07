package cn.gdeng.paycenter.entity.pay;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "member_baseinfo")
public class MemberBaseinfoEntity implements java.io.Serializable {

    private static final long serialVersionUID = -4938597388069282016L;

    /* 会员ID */
    private Long memberId;

    /* 会员账号 */
    private String account;

    /* 密码 */
    private String password;

    /**
     * 标示位, 会员类别（1谷登农批，2农速通，3农批宝，4.产地供应商，5.门岗， 其余待补）
     */
    private Integer level;

    /* 昵称 */
    private String nickName;

    /* 真是姓名 */
    private String realName;

    @Column(name = "realName")
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    /* 性别 */
    private Integer sex;
    /* 生日 */
    private Date birthday;
    // 手机
    private String mobile;
    // 电话
    private String telephone;
    // 邮箱
    private String email;
    // qq
    private String qq;
    // 微信
    private String weixin;
    // 0表示管理后台创建的用户，1表示web前端注册的用户，2表示农速通注册的用户，3表示农商友-买家版注册的用户，4表示农商友-卖家版注册的用户，5表示门岗添加的用户 7pos刷卡
    private String openId;// 微信用户唯一标识
    private String regetype;

    // 记录用户注册的ip地址
    private String ip;

    // 邮编
    private String zipCode;
    // 微博
    private String weibo;
    // 会员头像路径
    private String icon;
    // 所属省
    private Long provinceId;
    // 所属市
    private Long cityId;
    // 所属区
    private Long areaId;
    // 详细地址
    private String address;
    // 经度
    private Double lon;
    // 纬度
    private Double lat;
    // 状态（0未启用，1启用，其他状态待补）
    private String status;

    private String createUserId;

    private Date createTime;

    private String updateUserId;

    private Date updateTime;

    private String device_tokens;

    private Integer userType;

    private Long integral;

    private String unionid;
    private String gh_id;
    
    //会员等级(0:普通会员，1:金牌供应商)
    private Integer memberGrade;
    
    //会员生效时间
    private Date validTime;
    
    //会员到期时间
    private Date expireTime;
    
    //商铺推荐(0:不推荐，1:推荐)
    private Integer shopRecommend;

    @Column(name = "unionid")
    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Column(name = "gh_id")
    public String getGh_id() {
        return gh_id;
    }

    public void setGh_id(String gh_id) {
        this.gh_id = gh_id;
    }

    @Column(name = "andupurl")
    private String andupurl;

    private Integer nsyUserType;// 农商友用户类型

    @Column(name = "nsyUserType")
    public Integer getNsyUserType() {
        return nsyUserType;
    }

    public void setNsyUserType(Integer nsyUserType) {
        this.nsyUserType = nsyUserType;
    }

    @Column(name = "andupurl")
    public String getAndupurl() {
        return andupurl;
    }

    public void setAndupurl(String andupurl) {
        this.andupurl = andupurl;
    }

    private Integer subShow;

    @Column(name = "subShow")
    public Integer getSubShow() {
        return subShow;
    }

    public void setSubShow(Integer subShow) {
        this.subShow = subShow;
    }

    @Column(name = "integral")
    public Long getIntegral() {
        return integral;
    }

    public void setIntegral(Long integral) {
        this.integral = integral;
    }

    @Id
    @Column(name = "memberId", unique = true, nullable = false)
    public Long getMemberId() {

        return this.memberId;
    }

    public void setMemberId(Long memberId) {

        this.memberId = memberId;
    }

    @Column(name = "account")
    public String getAccount() {

        return this.account;
    }

    public void setAccount(String account) {

        this.account = account;
    }

    @Column(name = "password")
    public String getPassword() {

        return this.password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    @Column(name = "level")
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Column(name = "nickName")
    public String getNickName() {

        return this.nickName;
    }

    public void setNickName(String nickName) {

        this.nickName = nickName;
    }

    @Column(name = "sex")
    public Integer getSex() {

        return this.sex;
    }

    public void setSex(Integer sex) {

        this.sex = sex;
    }

    @Column(name = "birthday")
    public Date getBirthday() {

        return this.birthday;
    }

    public void setBirthday(Date birthday) {

        this.birthday = birthday;
    }

    @Column(name = "mobile")
    public String getMobile() {

        return this.mobile;
    }

    public void setMobile(String mobile) {

        this.mobile = mobile;
    }
    
    @Column(name = "telephone")
    public String getTelephone() {

        return this.telephone;
    }

    public void setTelephone(String telephone) {

        this.telephone = telephone;
    }

    @Column(name = "email")
    public String getEmail() {

        return this.email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    @Column(name = "userType")
    public Integer getUserType() {
        return this.userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    @Column(name = "qq")
    public String getQq() {

        return this.qq;
    }

    public void setQq(String qq) {

        this.qq = qq;
    }

    @Column(name = "weixin")
    public String getWeixin() {

        return this.weixin;
    }

    public void setWeixin(String weixin) {

        this.weixin = weixin;
    }

    @Column(name = "openId")
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Column(name = "weibo")
    public String getWeibo() {

        return this.weibo;
    }

    public void setWeibo(String weibo) {

        this.weibo = weibo;
    }

    @Column(name = "icon")
    public String getIcon() {

        return this.icon;
    }

    public void setIcon(String icon) {

        this.icon = icon;
    }

    @Column(name = "provinceId")
    public Long getProvinceId() {

        return this.provinceId;
    }

    public void setProvinceId(Long provinceId) {

        this.provinceId = provinceId;
    }

    @Column(name = "cityId")
    public Long getCityId() {

        return this.cityId;
    }

    public void setCityId(Long cityId) {

        this.cityId = cityId;
    }

    @Column(name = "areaId")
    public Long getAreaId() {

        return this.areaId;
    }

    public void setAreaId(Long areaId) {

        this.areaId = areaId;
    }

    @Column(name = "address")
    public String getAddress() {

        return this.address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    @Column(name = "lon")
    public Double getLon() {

        return this.lon;
    }

    public void setLon(Double lon) {

        this.lon = lon;
    }

    @Column(name = "lat")
    public Double getLat() {

        return this.lat;
    }

    public void setLat(Double lat) {

        this.lat = lat;
    }

    @Column(name = "status")
    public String getStatus() {

        return this.status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    @Column(name = "createUserId")
    public String getCreateUserId() {

        return this.createUserId;
    }

    public void setCreateUserId(String createUserId) {

        this.createUserId = createUserId;
    }

    @Column(name = "createTime")
    public Date getCreateTime() {

        return this.createTime;
    }

    public void setCreateTime(Date createTime) {

        this.createTime = createTime;
    }

    @Column(name = "updateUserId")
    public String getUpdateUserId() {

        return this.updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {

        this.updateUserId = updateUserId;
    }

    @Column(name = "updateTime")
    public Date getUpdateTime() {

        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {

        this.updateTime = updateTime;
    }

    @Column(name = "regetype")
    public String getRegetype() {
        return regetype;
    }

    public void setRegetype(String regetype) {
        this.regetype = regetype;
    }

    @Column(name = "ip")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name = "device_tokens")
    public String getDevice_tokens() {
        return device_tokens;
    }

    public void setDevice_tokens(String device_tokens) {
        this.device_tokens = device_tokens;
    }

    @Column(name = "zipCode")
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Column(name = "memberGrade")
	public Integer getMemberGrade() {
		return memberGrade;
	}

	public void setMemberGrade(Integer memberGrade) {
		this.memberGrade = memberGrade;
	}

    @Column(name = "validTime")
	public Date getValidTime() {
		return validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}

    @Column(name = "expireTime")
	public Date getExpireTime() {
		return expireTime;
	}

    
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	@Column(name = "shopRecommend")
	public Integer getShopRecommend() {
		return shopRecommend;
	}

	public void setShopRecommend(Integer shopRecommend) {
		this.shopRecommend = shopRecommend;
	}

}