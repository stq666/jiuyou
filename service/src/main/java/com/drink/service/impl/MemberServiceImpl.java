package com.drink.service.impl;

import com.drink.cornerstone.constant.ConstantElement;
import com.drink.cornerstone.service.ServiceException;
import com.drink.cornerstone.util.DateUtil;
import com.drink.cornerstone.util.MD5;
import com.drink.cornerstone.util.StringUtil;
import com.drink.dao.*;
import com.drink.model.*;
import com.drink.module.Page;
import com.drink.module.TreeNode;
import com.drink.module.member.MemberVo;
import com.drink.module.member.RewordVo;
import com.drink.service.MemberService;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/5/2 0002.
 */
@Service("memberService")
public class MemberServiceImpl implements MemberService {
    Log log= LogFactory.getLog(MemberServiceImpl.class);
    @Autowired
    MemberMapper memberMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RewardMapper rewardMapper;
    @Autowired
    MemberLevelMapper memberLevelMapper;
    @Autowired
    PublicRowMapper publicRowMapper;
    @Override
    public Page<MemberVo> findPageMember(Page<MemberVo> page,byte ifmanager,String serialNumber) {
        try {
            int start = page.getCurrentNum();
            int end=page.getPageSize()> ConstantElement.pageSize?ConstantElement.pageSize:page.getPageSize();
            MemberVo vo=page.getObj();
            if(ifmanager==(byte)0){
                vo.setSerialnumber(serialNumber);
            }
            int totalsize=memberMapper.findCountByCondition(vo);
            page.calculate(totalsize, start, end);
            vo.setStart(page.getStartPos());
            vo.setLimit(page.getEndPos());
            List<MemberVo> list=memberMapper.findDataByCondition(vo);
            page.setDatas(list);
//            if(ifmanager==(byte)1){//获取奖励　TODO
//                if(list!=null && list.size()>0){
//                    for(MemberVo mvo:list){
//                        if(mvo==null){continue;}
//                        List<String> allSerialNumber = new ArrayList<>();
//                        allSerialNumber.addAll(getSerialNumberByCurrentSerialNumber(mvo.getSerialnumber()));
//                        mvo.setTotalNumber(allSerialNumber.size());
//                        Map map = getTotalMoneyCurrentDay(mvo.getSerialnumber());
//                        mvo.setTotalMoney(Integer.valueOf(String.valueOf(map.get("totalMoney"))));
//                        mvo.setRewardStatus(Byte.valueOf(String.valueOf(map.get("rewardStatus"))));
//                    }
//                }
//                page.setDatas(list);
//            }
            return page;
        } catch (ServiceException e) {
            log.error(e.getMessage());
            throw new ServiceException(ConstantElement.commonError);
        }
    }

    /**
     * 获取自己介绍人的编号集合
     * @param serialNumber
     * @return
     */
    private List<String> findSubSerialNumber(String serialNumber) {
        return publicRowMapper.findSubSerialNumber(serialNumber);
    }
//    public Page<MemberVo> findPageMember(Page<MemberVo> page,byte ifmanager) {
//        try {
//            int start = page.getCurrentNum();
//            int end=page.getPageSize()> ConstantElement.pageSize?ConstantElement.pageSize:page.getPageSize();
//            MemberVo vo=page.getObj();
//            if(ifmanager==(byte)0){//非管理员
//                //获取自己的三级代理人的所有编码
//                MemberVo memberVo = memberMapper.selectById(vo.getId());
//                //根据当前登录人的编码获取自己的三级代理的编码
//                List<String> allSerialNumber = new ArrayList<>();
//                allSerialNumber.add(memberVo.getSerialnumber());
//                allSerialNumber.addAll(getSerialNumberByCurrentSerialNumber(memberVo.getSerialnumber()));
//                vo.setAllSerialNumber(allSerialNumber);
//                int totalsize=memberMapper.findCountByCondition(vo);
//                page.calculate(totalsize, start, end);
//                vo.setStart(page.getStartPos());
//                vo.setLimit(page.getEndPos());
//                List<MemberVo> list=memberMapper.findDataByCondition(vo);
//                page.setDatas(list);
//            }else{//管理员
//                int totalsize=memberMapper.findCountByCondition(vo);
//                page.calculate(totalsize, start, end);
//                vo.setStart(page.getStartPos());
//                vo.setLimit(page.getEndPos());
//                List<MemberVo> list=memberMapper.findDataByCondition(vo);
//                if(list!=null && list.size()>0){
//                    for(MemberVo mvo:list){
//                        if(mvo==null){continue;}
//                        List<String> allSerialNumber = new ArrayList<>();
//                        allSerialNumber.addAll(getSerialNumberByCurrentSerialNumber(mvo.getSerialnumber()));
//                        mvo.setTotalNumber(allSerialNumber.size());
//                        Map map = getTotalMoneyCurrentDay(mvo.getSerialnumber());
//                        mvo.setTotalMoney(Integer.valueOf(String.valueOf(map.get("totalMoney"))));
//                        mvo.setRewardStatus(Byte.valueOf(String.valueOf(map.get("rewardStatus"))));
//                    }
//                }
//                page.setDatas(list);
//            }
//
//            return page;
//        } catch (ServiceException e) {
//            log.error(e.getMessage());
//            throw new ServiceException(ConstantElement.commonError);
//        }
//    }

    private Map getTotalMoneyCurrentDay(String serialnumber) {
        Map map = new HashMap();
        int totalMoney = 0;
        byte rewardStatus = 0;
        List<Reward> rewards = rewardMapper.selectBySerialNumber(serialnumber,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        if(rewards!=null && rewards.size()>0){
            for(Reward reward:rewards){
                if(reward==null){continue;}
                totalMoney+=reward.getRewardmoney();
            }
            rewardStatus = rewards.get(0).getStatus();
        }
        map.put("totalMoney",totalMoney);
        map.put("rewardStatus",rewardStatus);
        return map;

    }


    private List<String> getSerialNumberByCurrentSerialNumber(String serialnumber) {
        List<String>allSers = new ArrayList<>();
        List<String>subs1 = memberLevelMapper.selectDirectSub(serialnumber);
        if(subs1!=null && subs1.size()>0){
            for(String sub1:subs1){
                if(StringUtil.isNull(sub1)){continue;}
                allSers.add(sub1);
                List<String>subs2 = memberLevelMapper.selectDirectSub(sub1);
                if(subs2!=null && subs2.size()>0){
                    for(String sub2:subs2){
                        if(StringUtil.isNull(sub2)){continue;}
                        allSers.add(sub2);
                        List<String>subs3 = memberLevelMapper.selectDirectSub(sub2);
                        if(subs3!=null && subs3.size()>0){
                            for(String sub3:subs3){
                                if(StringUtil.isNull(sub3)){continue;}
                                allSers.add(sub3);
                            }
                        }
                    }
                }
            }
        }
        return allSers;
    }
//    private void getSerialNumberByCurrentSerialNumber(String serialnumber,List<String> sers) {
//        List<String>subs = memberLevelMapper.selectDirectSub(serialnumber);
//        if(subs!=null && subs.size()>0){
//            for(String sub:subs){
//                if(StringUtil.isNull(sub)){continue;}
//                sers.add(sub);
//                getSerialNumberByCurrentSerialNumber(sub,sers);
//            }
//        }
//    }

    @Override
    public void saveMember(MemberVo vo) {
        //首先判断下级是否已经拥有了3个成员，如果已经满3个了，就不能再在这个会员编号下注册会员了
//        int count = memberMapper.selectCountBySerialNumber(vo.getPserialnumber());
        int count = memberLevelMapper.selectCountBySerialNumber(vo.getPserialnumber(),(byte)1);
        if(count<5){//既放在公排下，又放在层

        }
        //首先保存新注册的会员
        Member member = new Member();
        BeanUtils.copyProperties(vo,member);
        member.setStatus((byte)0);
        memberMapper.insertMember(member);
        Long memberId = memberMapper.selectId();
        //保存用户
        User user = new User();
        user.setLoginname(vo.getLoginname());
        user.setPassword(new MD5().getMD5ofStr("123456"));
        user.setStatus((byte)0);
        user.setIfmanager((byte)0);
        user.setMemberid(memberId);
        user.setCreatetime(new Date());
        userMapper.insertUser(user);
        if(vo.getFlag()==(byte)0){
            //第一步：保存父子级关系
            if(count<6){
                saveMemberLevel(vo.getSerialnumber(),vo.getPserialnumber());
            }
            savePublicRow(vo.getSerialnumber(),vo.getPserialnumber());
            //第二步：统计奖励
//            saveReward(vo);
        }
    }

    /**
     * 保存公排
     * @param serialnumber
     * @param pserialnumber
     */
    private void savePublicRow(String serialnumber, String pserialnumber) {
        PublicRow pr = new PublicRow();
        pr.setSerialnumber(serialnumber);
        pr.setPserialnumber(pserialnumber);
        pr.setBord((byte)1);
        publicRowMapper.insertPublicRow(pr);

    }
//    public void saveMember(MemberVo vo) {
//        //首先判断下级是否已经拥有了3个成员，如果已经满3个了，就不能再在这个会员编号下注册会员了
////        int count = memberMapper.selectCountBySerialNumber(vo.getPserialnumber());
//        int count = memberLevelMapper.selectCountBySerialNumber(vo.getPserialnumber(),(byte)1);
//        if(count>=3){
//            throw new ServiceException("此会员下已经有3个注册会员了，不能在此会员下注册了");
//        }
//        //首先保存新注册的会员
//        Member member = new Member();
//        BeanUtils.copyProperties(vo,member);
//        member.setStatus((byte)0);
//        memberMapper.insert(member);
//        Long memberId = memberMapper.selectId();
//        //保存用户
//        User user = new User();
//        user.setLoginname(vo.getLoginname());
//        user.setPassword(new MD5().getMD5ofStr("123456"));
//        user.setStatus((byte)0);
//        user.setIfmanager((byte)0);
//        user.setMemberid(memberId);
//        user.setCreatetime(new Date());
//        userMapper.insert(user);
//        if(vo.getFlag()==(byte)0){
//            //第一步：保存父子级关系
//            saveMemberLevel(vo.getSerialnumber(),vo.getPserialnumber());
//            //第二步：统计奖励
//            saveReward(vo);
//        }
//    }

    /**
     * 保存奖励
     * @param vo
     */
    private void saveReward(MemberVo vo) {
        //添加上级的奖励和额外的50元奖金,只奖励三级
        //第一级：自己的上级（称为1），第一级则额外奖励50元
        String pserinalNumber = saveOneReward(vo.getPserialnumber(),vo.getCreateuser());
        if(StringUtil.isNotNull(pserinalNumber)){
            //第二级：如果1还有上级，则奖励1的上级（称为11）
            String ppserinalNumber = saveTwoReward(pserinalNumber,vo.getCreateuser());
            if(StringUtil.isNotNull(ppserinalNumber)){
                //第三级：如果11还有上级，则奖励11的上级（称为111）
                saveThreeReward(ppserinalNumber,vo.getCreateuser());
            }
        }
    }

    /**
     * 保存上下级关系
     * @param serialnumber
     * @param pserialnumber
     */
    private void saveMemberLevel(String serialnumber, String pserialnumber) {
        MemberLevel ml = new MemberLevel();
        ml.setSerialnumber(serialnumber);
        ml.setPserialnumber(pserialnumber);
        ml.setBord((byte)1);
        memberLevelMapper.insertMemberLevel(ml);

    }


    /**
     * 奖励第三层
     * @param ppserinalNumber
     * @param createuser
     */
    private void saveThreeReward(String ppserinalNumber, Long createuser) {
        Reward reward = new Reward();
        reward.setMemberserialnumber(ppserinalNumber);
        reward.setRewardmoney(200);
        Date date = new Date();
        reward.setRewardtime(date);
        reward.setCreatetime(date);
        reward.setCreateuser(createuser);
        reward.setType((byte)0);
        reward.setStatus((byte)0);
        reward.setLevel((byte)3);
        rewardMapper.insert(reward);
    }

    /**
     * 奖励第二层
     * @param pserinalNumber
     * @param createuser
     * @return
     */
    private String saveTwoReward(String pserinalNumber, Long createuser) {
        Reward reward = new Reward();
        reward.setMemberserialnumber(pserinalNumber);
        reward.setRewardmoney(150);
        Date date = new Date();
        reward.setRewardtime(date);
        reward.setCreatetime(date);
        reward.setCreateuser(createuser);
        reward.setType((byte) 0);
        reward.setStatus((byte) 0);
        reward.setLevel((byte)2);
        rewardMapper.insert(reward);
        return memberLevelMapper.selectPserialNumberBySerialNumber(pserinalNumber);
    }

    /**
     * 奖励第一层
     * @param pserialnumber
     * @param createuser
     * @return
     */
    private String saveOneReward(String pserialnumber, Long createuser) {
        Reward reward = new Reward();
        reward.setMemberserialnumber(pserialnumber);
        reward.setRewardmoney(280);
        Date date = new Date();
        reward.setRewardtime(date);
        reward.setCreatetime(date);
        reward.setCreateuser(createuser);
        reward.setType((byte)0);
        reward.setStatus((byte)0);
        reward.setLevel((byte)1);
        rewardMapper.insert(reward);
        reward.setRewardmoney(50);
        reward.setType((byte)1);
        reward.setLevel((byte)0);
        rewardMapper.insert(reward);
        return memberLevelMapper.selectPserialNumberBySerialNumber(pserialnumber);
    }

    @Override
    public String findMaxSerialNumber() {
        String maxSerialNumber = memberMapper.selectMaxSerialNumber();
        if(StringUtil.isNull(maxSerialNumber)){
            return "88880000";
        }
        return String.valueOf(Long.valueOf(maxSerialNumber)+1);
    }

    @Override
    public MemberVo findLonginMember(Long id) {
        MemberVo vo = memberMapper.selectLoginMember(id);
        int totalMoney = 0;
        byte rewardStatus = 0;
        List<Reward> rewards = rewardMapper.selectBySerialNumber(vo.getSerialnumber(),new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        if(rewards!=null && rewards.size()>0){
            for(Reward reward:rewards){
                if(reward==null){continue;}
                totalMoney+=reward.getRewardmoney();
            }
            rewardStatus = rewards.get(0).getStatus();
        }
        vo.setRewardStatus(rewardStatus);
        vo.setTotalMoney(totalMoney);
        return vo;
    }

    @Override
    public List<TreeNode> findStructurlAllMemberById(Long id) {
        List<TreeNode> list = new ArrayList<>();
        MemberVo memberVo = memberMapper.selectById(id);
        list.add(saveMemberToTreeNode(memberVo));
        List<MemberVo>mems1 = memberLevelMapper.selectMemberByPerialNumber(memberVo.getSerialnumber());
        if(mems1!=null && mems1.size()>0){
            for(MemberVo mem1:mems1){
                if(mem1==null){continue;}
                mem1.setPid(id);
                list.add(saveMemberToTreeNode(mem1));
                List<MemberVo> mems2 = memberLevelMapper.selectMemberByPerialNumber(mem1.getSerialnumber());
                if(mems2!=null && mems2.size()>0){
                    for(MemberVo mem2:mems2){
                        if(mem2==null){continue;}
                        mem2.setPid(mem1.getId());
                        list.add(saveMemberToTreeNode(mem2));
                        List<MemberVo> mems3 = memberLevelMapper.selectMemberByPerialNumber(mem2.getSerialnumber());
                        if(mems3!=null && mems3.size()>0){
                            for(MemberVo mem3:mems3){
                                if(mem3==null){continue;}
                                mem3.setPid(mem2.getId());
                                list.add(saveMemberToTreeNode(mem3));
                            }
                        }
                    }
                }
            }
        }
        return list;

    }

    @Override
    public List<RewordVo> showRewordDetail(String serialnumber) {
        List<RewordVo> list = new ArrayList<>();
        List<Reward> rewards = rewardMapper.selectBySerialNumber(serialnumber,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        int oneMoney = 0;
        int twoMoney = 0;
        int threeMoney = 0;
        int otherMoney = 0;
        if(rewards!=null && rewards.size()>0){
            for(Reward reward:rewards){
                if(reward==null){continue;}
                if(reward.getRewardmoney()==280){//第一层
                    oneMoney+=reward.getRewardmoney();
                }else if(reward.getRewardmoney()==150){//第二层
                    twoMoney+=reward.getRewardmoney();
                }else if(reward.getRewardmoney()==200){//第三层
                    threeMoney+=reward.getRewardmoney();
                }else if(reward.getRewardmoney()==50){//第三层
                    otherMoney+=reward.getRewardmoney();
                }

            }
        }
        list.add(new RewordVo("一级代理奖励",oneMoney));
        list.add(new RewordVo("二级代理奖励",twoMoney));
        list.add(new RewordVo("三级代理奖励",threeMoney));
        list.add(new RewordVo("额外奖励",otherMoney));
        return list;

    }

    @Override
    public void updateRewardStatus(String serialnumber) {
        rewardMapper.updateStatusBySerialnumber(serialnumber,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    @Override
    public void updateMember(MemberVo vo) {
        memberMapper.updateByMember(vo);

    }

    @Override
    public void updatePassword(Long[] ids) {
        if(ids!=null && ids.length>0){
            for(Long id:ids){
                if(id==null){continue;}
                userMapper.updatePasswordById(id,new MD5().getMD5ofStr("12345"));
            }
        }

    }

    @Override
    public List<RewordVo> findEveryDayMoney(String serialnumber) {
        List<RewordVo> list = rewardMapper.selectEveryDayMoney(serialnumber);
        return list;
    }

    @Override
    public void updateRewardStatusAndRewardtime(String serialnumber, String rewardtime) {
        rewardMapper.updateStatusBySerialnumber(serialnumber,rewardtime);
    }

    private TreeNode saveMemberToTreeNode(MemberVo vo) {
        TreeNode node = new TreeNode();
        node.setId(vo.getId());
        if(vo.getPid()==null || vo.getPid()==-1){
            node.setpId(-1l);
        }else{
            node.setpId(vo.getPid());
        }

        node.setName(vo.getName());
        node.setSerialNumber(vo.getSerialnumber());
        node.setRegisterDate(new SimpleDateFormat("yyyy-MM-dd").format(vo.getCreatetime()));
        return node;
    }

}
