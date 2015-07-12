package com.keithsmyth.noms.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keithsmyth.noms.App;
import com.keithsmyth.noms.R;
import com.keithsmyth.noms.adapter.EntryAdapter;
import com.keithsmyth.noms.data.DataManager;
import com.keithsmyth.noms.data.IDataManager;
import com.keithsmyth.noms.model.Entry;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static butterknife.ButterKnife.findById;


/**
 * A placeholder fragment containing a simple view.
 */
public class EntryListFragment extends Fragment {

    private Navigatable nav;
    private final SubscriptionManager subscriptionManager = new SubscriptionManager();

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        nav = (Navigatable) activity;
    }

    @Override public void onDetach() {
        super.onDetach();
        nav = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entry_list, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        final RecyclerView entriesRecycler = findById(view, R.id.rcy_entries);
        entriesRecycler.setHasFixedSize(true);
        entriesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        final EntryAdapter entryAdapter = new EntryAdapter();
        entriesRecycler.setAdapter(entryAdapter);

        subscriptionManager.add(App.getDataManager().getEntryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(entryAdapter));

        subscriptionManager.add(entryAdapter.getEntryClickObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Entry>() {
                    @Override public void call(Entry entry) {
                        final Fragment fragment = EntryFragment.createForExisting(entry.getObjectId());
                        nav.next(fragment);
                    }
                }));
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_add) public void onAddClick() {
        nav.next(EntryFragment.createForNew());
    }

    @Override public void onDestroy() {
        super.onDestroy();
        subscriptionManager.unsubscribe();
    }
}
