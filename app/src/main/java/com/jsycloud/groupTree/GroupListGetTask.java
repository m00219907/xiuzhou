package com.jsycloud.groupTree;

import com.jsycloud.groupTree.bean.TreeNode;

import android.os.AsyncTask;

public class GroupListGetTask extends AsyncTask<Void,Void,TreeNode> {
    private GroupListManager mGroupListManager = null;

    private byte[] szCoding = null;

    private IOnSuccessListener mOnSuccessListener;

    public interface IOnSuccessListener {
        public void onSuccess(boolean success, int errCode);
    }

    public GroupListGetTask() {
        mGroupListManager = GroupListManager.getInstance();
    }

    public void setListener(IOnSuccessListener onSuccessListener) {
        mOnSuccessListener = onSuccessListener;
    }

    @Override
   protected void onPreExecute() {
      super.onPreExecute();
   }

   @Override
   protected TreeNode doInBackground(Void... arg0) {   
       mGroupListManager.setFinish(false);
       szCoding = mGroupListManager.loadDGroupInfoLayered();
       mGroupListManager.getGroupList(szCoding, mGroupListManager.getRootNode());
       mGroupListManager.setFinish(true);
       if (mOnSuccessListener != null) {
         mOnSuccessListener.onSuccess(true, 0);
       }
       return mGroupListManager.getRootNode();
      
   }

   /*@Override
   protected void onPostExecute(TreeNode result) {
      super.onPostExecute(result);
      mGroupListManager.setFinish(false);
   }*/
   
}
