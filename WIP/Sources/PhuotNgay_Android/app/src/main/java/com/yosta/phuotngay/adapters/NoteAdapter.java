package com.yosta.phuotngay.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.models.note.Note;
import com.yosta.phuotngay.viewmodel.NoteViewHolder;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private Context mContext;
    private List<Note> behaviorList;

    public NoteAdapter(Context context) {
        this.mContext = context;
        this.behaviorList = new ArrayList<>();
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext)
                .inflate(R.layout.view_item_note, parent, false);
        return new NoteViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, final int position) {
    }

    @Override
    public int getItemCount() {
        return behaviorList.size();
    }


    public void adds(List<Note> notes) {
        int positionStart = behaviorList.size() - 1;
        behaviorList.addAll(notes);
        notifyItemRangeChanged(positionStart, notes.size());
    }

    public void add(Note note) {
        behaviorList.add(note);
        notifyItemChanged(behaviorList.size() - 1);
    }

    public void clear() {
        behaviorList.clear();
        notifyDataSetChanged();
    }

}

