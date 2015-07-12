package com.keithsmyth.noms.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.keithsmyth.noms.R;
import com.keithsmyth.noms.model.Entry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * @author keithsmyth
 */
public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder>
        implements Action1<Collection<Entry>> {

    private final List<Entry> entryList = new ArrayList<>();
    private final PublishSubject<Entry> entryClickSubject = PublishSubject.create();

    public Observable<Entry> getEntryClickObservable() {
        return entryClickSubject;
    }

    @Override public void call(Collection<Entry> list) {
        entryList.clear();
        entryList.addAll(list);
        notifyDataSetChanged();
    }

    @Override public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.item_entry, parent, false);
        return new EntryViewHolder(view);
    }

    @Override public void onBindViewHolder(EntryViewHolder holder, int position) {
        holder.bind(entryList.get(position), entryClickSubject);
    }

    @Override public int getItemCount() {
        return entryList.size();
    }

    public static class EntryViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txt_date) TextView dateText;
        @Bind(R.id.txt_category) TextView categoryText;
        @Bind(R.id.txt_amount) TextView amountText;
        @Bind(R.id.txt_description) TextView descriptionText;
        @Bind(R.id.chk_in_rules) CheckBox rulesCheck;
        @Bind(R.id.txt_comment) TextView commentText;

        public EntryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Entry entry, final PublishSubject<Entry> entryClickSubject) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    entryClickSubject.onNext(entry);
                }
            });
            dateText.setText(entry.getDate().toString());
            categoryText.setText(entry.getCategory());
            amountText.setText(String.valueOf(entry.getAmount()));
            descriptionText.setText(entry.getDescription());
            rulesCheck.setChecked(entry.isInRules());
            commentText.setText(entry.getComment());
        }
    }
}
