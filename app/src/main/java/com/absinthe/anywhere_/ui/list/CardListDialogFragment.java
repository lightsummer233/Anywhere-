package com.absinthe.anywhere_.ui.list;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.absinthe.anywhere_.AnywhereApplication;
import com.absinthe.anywhere_.adapter.applist.AppListAdapter;
import com.absinthe.anywhere_.model.AnywhereEntity;
import com.absinthe.anywhere_.model.AnywhereType;
import com.absinthe.anywhere_.model.AppListBean;
import com.absinthe.anywhere_.view.AnywhereDialogBuilder;
import com.absinthe.anywhere_.view.AnywhereDialogFragment;
import com.absinthe.anywhere_.viewbuilder.entity.CardListDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class CardListDialogFragment extends AnywhereDialogFragment {
    private Context mContext;
    private CardListDialogBuilder mBuilder;
    private AppListAdapter.OnItemClickListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = getActivity();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AnywhereDialogBuilder builder = new AnywhereDialogBuilder(mContext);
        mBuilder = new CardListDialogBuilder(mContext);
        if (mListener != null) {
            mBuilder.setOnItemClickListener(mListener);
        }
        initView();

        return builder.setView(mBuilder.getRoot())
                .create();
    }

    public void setOnItemClickListener(AppListAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    private void initView() {
        List<AppListBean> listBeans = new ArrayList<>();

        List<AnywhereEntity> list = AnywhereApplication.sRepository.getAllAnywhereEntities().getValue();
        if (list != null) {
            for (AnywhereEntity ae : list) {
                if (ae.getAnywhereType() == AnywhereType.URL_SCHEME || ae.getAnywhereType() == AnywhereType.IMAGE) {
                    listBeans.add(new AppListBean(ae.getAppName(), ae.getParam2(), ae.getParam1(), ae.getAnywhereType()));
                } else {
                    listBeans.add(new AppListBean(ae.getAppName(), ae.getParam1(), ae.getParam2(), ae.getAnywhereType()));
                }
            }
            mBuilder.mAdapter.setList(listBeans);
        }
    }
}