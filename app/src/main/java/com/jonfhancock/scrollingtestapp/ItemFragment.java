package com.jonfhancock.scrollingtestapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemFragment extends Fragment {
    private static final String ARG_POSITION = "ARG_POSITION";

    private int mPosition;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private ItemAdapter mItemAdapter;

    private OnFragmentInteractionListener mListener;

    public static ItemFragment newInstance(int position) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public ItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_POSITION);
        }
        mLayoutManager = new LinearLayoutManager(getActivity());
        mItemAdapter = new ItemAdapter(getActivity());
        mItemAdapter.setListener(new ItemAdapter.OnImageClickListner() {
            @Override
            public void onImageClicked(String url) {
                mListener.onFragmentInteraction(url);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_item, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        return root;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mItemAdapter);
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String uri);
    }

    public static class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder>{
        private List<RecyclerItem> mItems;
        private RequestManager mPicasso;
        private LayoutInflater mInflater;
        private OnImageClickListner mListener;

        public void setListener(OnImageClickListner listener) {
            mListener = listener;
        }

        public interface OnImageClickListner{
            public void onImageClicked(String url);
        }

        public ItemAdapter(Activity activity) {
            mItems = new ArrayList<>();
            mInflater = activity.getLayoutInflater();
            mPicasso = Glide.with(activity);
            Random random = new Random();
            int total = random.nextInt(20);
            while (total<1){
                total = random.nextInt(20);
            }
            for(int i = 0; i< total; i++){
                mItems.add(new RecyclerItem(i));
            }
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = mInflater.inflate(R.layout.item_card, parent, false);
            ItemViewHolder holder = new ItemViewHolder(root);
            return holder;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            mPicasso.load(mItems.get(position).thumbUrl).placeholder(R.drawable.conent_placeholder).into(holder.image);
            holder.root.setTag(mItems.get(position).thumbUrl);
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null) {
                        String url = (String) v.getTag();
                        mListener.onImageClicked(url);
                    }

                }
            });

        }


        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        ViewGroup root;
        ImageView image;
        public ItemViewHolder(View itemView) {
            super(itemView);
            root = (ViewGroup) itemView;
            image = (ImageView) root.findViewById(R.id.image);
        }
    }

    public static  class RecyclerItem {
        public RecyclerItem(int position) {
            this.position = position;
            this.thumbUrl = "http://lorempixel.com/1280/720/?"+position;
        }

        public int position;
        public String thumbUrl;

    }
}
