package com.jsycloud.groupTree;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.util.Log;

import com.dh.DpsdkCore.Dep_Info_Ex_t;
import com.dh.DpsdkCore.Dep_Info_t;
import com.dh.DpsdkCore.Device_Info_Ex_t;
import com.dh.DpsdkCore.Enc_Channel_Info_Ex_t;
import com.dh.DpsdkCore.Get_Channel_Info_Ex_t;
import com.dh.DpsdkCore.Get_Dep_Count_Info_t;
import com.dh.DpsdkCore.Get_Dep_Info_t;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.Return_Value_ByteArray_t;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.dh.DpsdkCore.dpsdk_change_type_e;
import com.dh.DpsdkCore.dpsdk_node_type_e;
import com.dh.DpsdkCore.fDPSDKDevStatusCallback;
import com.dh.DpsdkCore.fDPSDKDeviceChangeCallback;
import com.dh.DpsdkCore.fDPSDKNVRChnlStatusCallback;
import com.jsycloud.ir.xiuzhou.AppApplication;
import com.jsycloud.exception.IDpsdkCoreException;
import com.jsycloud.groupTree.bean.ChannelInfoExt;
import com.jsycloud.groupTree.bean.DeviceInfo;
import com.jsycloud.groupTree.bean.TreeNode;

public class GroupListManager {

    private static final String TAG = "GroupListManager";

    private static GroupListManager groupListManager;
    
    // 保存Device_Info_Ex_t  在线设备列表，用于通过设备呼叫号码来查询设备id
    private List<Device_Info_Ex_t> deviceExList = null;

    // 选中通道列表
    private List<ChannelInfoExt> channelList = null;

    // 超时时间
    private static final int DPSDK_CORE_DEFAULT_TIMEOUT = 600000;

    // 节点code
    private byte[] szCoding = null;

    // 根节点
    private TreeNode rootNode = null;

    // 返回的结果
    private int ret = -1;

    // 获取组织树任务
    private GroupListGetTask task = null;

    // 获取列表任务是否结束
    private boolean isFinish = false;

    // Get_Dep_Count_Info_t
    private Get_Dep_Count_Info_t gdcInfo = null;

    // Get_Dep_Info_t
    private Get_Dep_Info_t gdInfo = null;

    private GroupListGetTask.IOnSuccessListener onSuccessListener = null;
    
    // 根据id获取节点
    private HashMap<String, TreeNode> channelNodeIndex = null;

    public static synchronized GroupListManager getInstance() {
        if (groupListManager == null) {
            groupListManager = new GroupListManager();
        }
        return groupListManager;
    }

    public GroupListManager() {
        channelList = new ArrayList<ChannelInfoExt>();
        deviceExList = new ArrayList<Device_Info_Ex_t>();
        channelNodeIndex = new HashMap<String, TreeNode>();
    }

    public int getnPDLLHandle() {
        return AppApplication.get().getDpsdkHandle();
    }

    public void startGroupListGetTask() {
        channelNodeIndex.clear();
        // 获取组织树任务
        task = new GroupListGetTask();
        task.execute();
    }

    public void setGroupListGetListener(final GroupListGetTask.IOnSuccessListener onSuccessListener) {
        if (task == null)
            return;
        GroupListGetTask.IOnSuccessListener listener = new GroupListGetTask.IOnSuccessListener() {
            @Override
            public void onSuccess(boolean success, int errCode) {
                if (onSuccessListener != null) {
                    onSuccessListener.onSuccess(success, errCode);
                }
                task = null;
            }
        };
        task.setListener(listener);
    }

    public GroupListGetTask getTask() {
        return task;
    }

    public void setTask(GroupListGetTask task) {
        this.task = task;
    }

    fDPSDKDeviceChangeCallback fDevCallback = new fDPSDKDeviceChangeCallback() {
        @Override
        public void invoke(int nPDLLHandle, int nChangeType, byte[] szDeviceId, byte[] szDepCode, byte[] szNewOrgCode) {
            dPSDKDeviceChangeCallback(nPDLLHandle, nChangeType, szDeviceId, szDepCode, szNewOrgCode);
        }
    };

    fDPSDKDevStatusCallback fDevStatusCallback = new fDPSDKDevStatusCallback() {
        @Override
        public void invoke(int nPDLLHandle, byte[] szDeviceId, int nStatus) {
            String deviceId = new String(szDeviceId).trim();
            updateDevStatus(deviceId, nStatus, rootNode);
        }
    };
    fDPSDKNVRChnlStatusCallback fNvrChnlStatusCallback = new fDPSDKNVRChnlStatusCallback() {
        @Override
        public void invoke(int nPDLLHandle, byte[] szChnlId, int nStatus) {
     String chnlId = new String(szChnlId).trim();
         updateNVRChnlStatus(chnlId, nStatus, rootNode);
        }
    };

    /**
     * <p>
     * 组织树改变回调
     * </p>
     * 
     * @author fangzhihua 2014年7月3日 上午11:22:09
     * @param nPDLLHandle
     * @param nChangeType
     * @param szDeviceId
     * @param szDepCode
     * @param szNewOrgCode
     */
    private void dPSDKDeviceChangeCallback(int nPDLLHandle, int nChangeType, byte[] szDeviceId, byte[] szDepCode,
            byte[] szNewOrgCode) {
        switch (nChangeType) {
            case dpsdk_change_type_e.DPSDK_CORE_CHANGE_ADD_ORG:
            case dpsdk_change_type_e.DPSDK_CORE_CHANGE_DELETE_ORG:
            case dpsdk_change_type_e.DPSDK_CORE_CHANGE_MODIFY_ORG:
                if (task == null) {
                    startGroupListGetTask();
                    task.setListener(onSuccessListener);
                }
                break;
            case dpsdk_change_type_e.DPSDK_CORE_CHANGE_ADD_DEV:
            case dpsdk_change_type_e.DPSDK_CORE_CHANGE_DEL_DEV:
            case dpsdk_change_type_e.DPSDK_CORE_CHANGE_MODIFY_DEV:
                if (task == null) {
                    startGroupListGetTask();
                    task.setListener(onSuccessListener);
                }
                break;
            default:
                break;
        }

    }

    /**
     * <p>
     * 循环组织树设置
     * </p>
     * 
     * @author fangzhihua 2014年7月1日 下午8:22:17
     * @param deviceId
     * @param nStatus
     * @param curNode
     */
    public void updateDevStatus(String deviceId, int nStatus, TreeNode curNode) {
        if (curNode == null)
            return;
        if (curNode.getType() == 2 && curNode.getDeviceInfo().getDeviceId().equals(deviceId)) {
            curNode.getDeviceInfo().setStatus(nStatus);
            return;
        } else if (curNode.getType() == 1 || curNode.getType() == 0) {
            for (int i = 0; i < curNode.getChildren().size(); i++) {
                // 循环获取组和设备信息
                updateDevStatus(deviceId, nStatus, curNode.getChildren().get(i));
            }
        }
    }
    
    
    public void updateNVRChnlStatus(String chnlId, int nStatus, TreeNode curNode) {
        TreeNode chnlNode = null;
        synchronized (channelNodeIndex) {
           chnlNode = channelNodeIndex.get(chnlId);
        }
        if (chnlNode != null) {
            chnlNode.getChannelInfo().setState(nStatus);
        }
    }
    

    /**
     * <p>
     * 创建根节点
     * </p>
     * 
     * @author fangzhihua 2014-5-7 下午4:23:53
     * @return
     * @throws IDpsdkCoreException
     */
    public synchronized byte[] loadDGroupInfoLayered()  {
        // IDpsdkCore.DPSDK_SetDPSDKStatusCallback(getnPDLLHandle(), fCallback);
        IDpsdkCore.DPSDK_SetDPSDKDeviceChangeCallback(getnPDLLHandle(), fDevCallback);
        // 设备状态更新回调
        IDpsdkCore.DPSDK_SetDPSDKDeviceStatusCallback(getnPDLLHandle(), fDevStatusCallback);
        // nvr通道状态变更回调
        IDpsdkCore.DPSDK_SetDPSDKNVRChnlStatusCallback(getnPDLLHandle(), fNvrChnlStatusCallback);
        
        Return_Value_Info_t rvInfo2 = new Return_Value_Info_t();
        ret = IDpsdkCore.DPSDK_LoadDGroupInfo(getnPDLLHandle(), rvInfo2, DPSDK_CORE_DEFAULT_TIMEOUT);
  
        Boolean hasLogicorg = IDpsdkCore.DPSDK_HasLogicOrg(getnPDLLHandle());
        if (!hasLogicorg) {
            // 获取根节点的信息DPSDK_GetDGroupRootInfo
            Dep_Info_t dInfo = new Dep_Info_t();
            ret = IDpsdkCore.DPSDK_GetDGroupRootInfo(getnPDLLHandle(), dInfo);
            szCoding = dInfo.szCoding;
            // 建立树根
            rootNode = new TreeNode(new String(dInfo.szDepName), new String(dInfo.szCoding));
            Log.i("loadDGroupInfoLayered", new String(dInfo.szDepName) + new String(dInfo.szCoding) + ret);
        } else {
            Dep_Info_Ex_t dInfoExt = new Dep_Info_Ex_t();
            ret = IDpsdkCore.DPSDK_GetLogicRootDepInfo(getnPDLLHandle(),dInfoExt);
            szCoding = dInfoExt.szCoding;
            rootNode = new TreeNode(new String(dInfoExt.szDepName), new String(dInfoExt.szCoding));
            Log.i("loadDGroupInfoLayered", new String(dInfoExt.szDepName) + new String(dInfoExt.szCoding) + ret);
        }
        
        return szCoding;
    }

    /**
     * <p>
     * </p>
     * 
     * @author fangzhihua 2014-5-7 下午4:23:53
     * @return
     */
    public synchronized void getGroupList(byte[] coding, TreeNode curNode)  {
        if (curNode == null)
            return;
          Boolean hasLogicorg = IDpsdkCore.DPSDK_HasLogicOrg(getnPDLLHandle());
          if (hasLogicorg) {
            //业务组
            Return_Value_Info_t depNum = new Return_Value_Info_t();
            ret = IDpsdkCore.DPSDK_GetLogicDepNodeNum(getnPDLLHandle(), coding, dpsdk_node_type_e.DPSDK_CORE_NODE_DEP, depNum);
            //throwException("DPSDK_GetLogicDepNodeNum_EXCEPTION");
            Dep_Info_Ex_t dep_Info_Ex_t = new Dep_Info_Ex_t();
            TreeNode logicDepNode = null;
            for (int i = 0; i < depNum.nReturnValue; i++ ) {
                IDpsdkCore.DPSDK_GetLogicSubDepInfoByIndex(getnPDLLHandle(), coding, i, dep_Info_Ex_t);
                logicDepNode = new TreeNode(new String(dep_Info_Ex_t.szDepName).trim(), new String(dep_Info_Ex_t.szCoding).trim());
                // 设置为组
                logicDepNode.setType(1);
                logicDepNode.setParent(curNode);
                curNode.add(logicDepNode);
                Log.i(TAG, "name of logicDepNode" + new String(dep_Info_Ex_t.szDepName) + "number of logicDepNode" + depNum.nReturnValue);
       
            }
            //业务通道
            Return_Value_Info_t channelNum = new Return_Value_Info_t();
            ret = IDpsdkCore.DPSDK_GetLogicDepNodeNum(getnPDLLHandle(), coding, dpsdk_node_type_e.DPSDK_CORE_NODE_CHANNEL, channelNum);
            Return_Value_ByteArray_t return_Value_ByteArray_t = new Return_Value_ByteArray_t();
            
            int cNum = channelNum.nReturnValue;
            Enc_Channel_Info_Ex_t[] ecInfo = new Enc_Channel_Info_Ex_t[cNum];
            for (int j = 0; j < cNum; j++ ) {
                Enc_Channel_Info_Ex_t  enc_Channel_Info_Ex_t = new Enc_Channel_Info_Ex_t();
                IDpsdkCore.DPSDK_GetLogicID(getnPDLLHandle(), coding, j, true, return_Value_ByteArray_t);
                IDpsdkCore.DPSDK_GetChannelInfoById(getnPDLLHandle(), return_Value_ByteArray_t.szCodeID, enc_Channel_Info_Ex_t);
                ecInfo[j] = enc_Channel_Info_Ex_t;
            }
            Log.i(TAG, "cNum" + cNum);
            addChannelInfoNode(ecInfo, curNode);
            throwException("DPSDK_GetLogicDepNodeNum_EXCEPTION");
          
          } else {
            // 获取组织设备信息串数量DPSDK_GetDGroupCount
            gdcInfo = new Get_Dep_Count_Info_t();
            gdcInfo.szCoding = coding;   //通过组织节点信息，参看节点下的设备个数
            ret = IDpsdkCore.DPSDK_GetDGroupCount(getnPDLLHandle(), gdcInfo);  //先获取设备个数
            throwException("DPSDK_GETDGROUPCOUNT_EXCEPTION");
      
            // 获取组织设备信息串DPSDK_GetDGroupInfo
            gdInfo = new Get_Dep_Info_t(gdcInfo.nDepCount, gdcInfo.nDeviceCount);
            gdInfo.szCoding = coding;
            ret = IDpsdkCore.DPSDK_GetDGroupInfo(getnPDLLHandle(), gdInfo);
            throwException("DPSDK_GETDGROUPINFO_EXCEPTION");

            // 组织信息
            Dep_Info_t[] dInfo = gdInfo.pDepInfo;
            TreeNode depNode = null;
            for (int i = 0; i < dInfo.length; i++) { 
                depNode = new TreeNode(new String(dInfo[i].szDepName).trim(), new String(dInfo[i].szCoding).trim());
                // 设置为组
                depNode.setType(1);
                depNode.setParent(curNode);
                curNode.add(depNode);
            }

            // 设备信息
            Device_Info_Ex_t[] dInfo1 = gdInfo.pDeviceInfo;
            List<Device_Info_Ex_t> dInfo2 = new ArrayList<Device_Info_Ex_t>();
            int port = 0;
            // 设备在线离线排序
            for (int i = 0; i < dInfo1.length; i++) {
                if (dInfo1[i].nStatus == 2) {  //offline
                    dInfo2.add(dInfo1[i]);
                } else {
                    dInfo2.add(port, dInfo1[i]);  //在线设备在前，并且按顺序插入
                    port++;
                }
            }

            TreeNode devNode = null;
            DeviceInfo deviceInfo = null;
            String szid = null;
            String devName = null;
            for (int i = 0; i < dInfo2.size(); i++) {
                szid = new String(dInfo2.get(i).szId).trim();
                // 过滤szid为空的数据
                if (szid.equals("")) {
                    continue;
                }
                

                deviceExList.add(dInfo2.get(i));  //添加在线设备信息

                               
                //将设备信息组装为treenode设备节点
                devName = new String(dInfo2.get(i).szName).trim();
                Log.i("GroupListManager", "devName " + devName);
                devNode = new TreeNode(devName, szid);
                deviceInfo = new DeviceInfo();
                deviceInfo.setDeviceId(szid);
                deviceInfo.setDeviceName(devName);
                deviceInfo.setDeviceIp(new String(dInfo2.get(i).szIP));
                deviceInfo.setDevicePort(dInfo2.get(i).nPort);
                deviceInfo.setUserName(new String(dInfo2.get(i).szUser));
                deviceInfo.setPassWord(new String(dInfo2.get(i).szPassword));
                deviceInfo.setChannelCount(dInfo2.get(i).nEncChannelChildCount);
                deviceInfo.setFactory(dInfo2.get(i).nFactory);
                deviceInfo.setStatus(dInfo2.get(i).nStatus);
                deviceInfo.setdeviceType(dInfo2.get(i).nDevType);
                Log.i(TAG, "deviceType:" + dInfo2.get(i).nDevType);
                devNode.setDeviceInfo(deviceInfo);
                
                devNode.setType(2);     // 设置为设备类型
                devNode.setParent(curNode);
                curNode.add(devNode);
                
                // 获取通道信息     通过设备id-->获取该设备下的通道信息  IDpsdkCore.DPSDK_GetChannelInfoEx
                Get_Channel_Info_Ex_t gcInfo = new Get_Channel_Info_Ex_t(dInfo2.get(i).nEncChannelChildCount); //设备信息中有编码子通道个数信息
                gcInfo.szDeviceId = dInfo2.get(i).szId;
                ret = IDpsdkCore.DPSDK_GetChannelInfoEx(getnPDLLHandle(), gcInfo);
                Log.d(TAG, "DPSDK_GetChannelInfoEx=" + ret + ",gcInfo.szDeviceId=" + szid);
                throwException("DPSDK_GetChannelInfoEx_EXCEPTION");
                
                Enc_Channel_Info_Ex_t[] ecInfo = gcInfo.pEncChannelnfo;
                          
                addChannelInfoNode(ecInfo, devNode);         
            }
          }
        
        for (int i = 0; i < curNode.getDepChildren().size(); i++) {
            // 循环获取组和设备信息
            getGroupList(curNode.getDepChildren().get(i).getValue().getBytes(), curNode.getDepChildren().get(i));
        }

    }

    private void addChannelInfoNode(Enc_Channel_Info_Ex_t[] ecInfo, TreeNode devNode) {
          
          TreeNode chanNode = null;
          String name = "";
          String szId = "";
          // int position = 0;
          for (int j = 0; j < ecInfo.length; j++) {  //ecInfo 特定设备下的编码通道信息
              // 处理如果通道名称为空，则默认显示：通道1
              name = new String(ecInfo[j].szName).trim();
              szId = new String(ecInfo[j].szId).trim();
              // 过滤szid为空的数据
              if (szId.equals("")) {
                  continue;
              }
              // pdsdk带过来的是\u0000.空格是
              // position = new String(ecInfo[j].szId).indexOf('\u0000');
              // if (position > 0) {
              // szId = new String(ecInfo[j].szId).substring(0, position);
              // } else {
              // szId = new String(ecInfo[j].szId);
              // }
              chanNode = new TreeNode(name, szId);
              ChannelInfoExt channelInfo = new ChannelInfoExt();
              byte[] szDevId = new byte[64];
              Return_Value_Info_t return_Value_Info_t = new Return_Value_Info_t();
              int ret = IDpsdkCore.DPSDK_GetDevIdByChnId(getnPDLLHandle(), ecInfo[j].szId, szDevId);
              
      /*        if (ecInfo[j].nChnlType == dpsdk_dev_type_e.DEV_TYPE_NVR) {
                  IDpsdkCore.DPSDK_QueryNVRChnlStatus(getnPDLLHandle(), ecInfo[j].szId, DPSDK_CORE_DEFAULT_TIMEOUT); 
              } else {*/
                  IDpsdkCore.DPSDK_GetChannelStatus(getnPDLLHandle(), ecInfo[j].szId, return_Value_Info_t);  //根据CemeraId获取通道状态，不支持NVR通道状态获取 
             // }
              
              channelInfo.setState(return_Value_Info_t.nReturnValue);
              channelInfo.setDeviceId(new String(szDevId));
              channelInfo.setDevType(ecInfo[j].nCameraType);
              channelInfo.setSzId(szId);
              channelInfo.setSzName(name);
              channelInfo.setRight(ecInfo[j].nRight);
              chanNode.setChannelInfo(channelInfo);
              
//              Log.e(TAG, "================channel node =================="+"           "+new String(szDevId));   
              
              chanNode.setType(3);  // 设置为通道
              chanNode.setParent(devNode);
              synchronized (channelNodeIndex) {
                  channelNodeIndex.put(szId.trim(), chanNode);
              }
              devNode.add(chanNode);
              Log.i(TAG, "name of logicChannelNode" + name + "number of logicChannelNode" + ecInfo.length);
          }
    }

    private void throwException(String str) {
        if (ret != 0) {
            try {
                throw new IDpsdkCoreException(str, ret);
            } catch (IDpsdkCoreException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }
        }
    }

    public TreeNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public List<Device_Info_Ex_t> getDeviceExList(){
        return deviceExList;
    }

    public List<ChannelInfoExt> getChannelList() {
        return channelList;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public void setOnSuccessListener(GroupListGetTask.IOnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

}
