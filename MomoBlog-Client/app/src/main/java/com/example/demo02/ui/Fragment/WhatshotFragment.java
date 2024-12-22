package com.example.demo02.ui.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo02.R;
import com.example.demo02.entity.Blog;
import com.example.demo02.ui.Adapter.BlogAdapter;
import com.example.demo02.utils.Network.BlogRepository;

import java.util.ArrayList;
import java.util.List;

public class WhatshotFragment extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BlogAdapter adapter;
    private final List<Blog> blogList = new ArrayList<Blog>();
    private int currentPage = 1;
    private boolean isLoading = false;
    private BlogRepository blogRepository;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        blogRepository = new BlogRepository(getString(R.string.sort_by_likeCount));

        View view = inflater.inflate(R.layout.fragment_whatshot, container, false);
        recyclerView = view.findViewById(R.id.whatshot_recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.whatshot_swipeRefreshLayout);

        adapter = new BlogAdapter(blogList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        isLoading = false;
        currentPage = 1;

        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 1;
            fetchBlogData(currentPage, true);
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    isLoading = true;
                    currentPage++;
                    fetchBlogData(currentPage, false);
                }
            }
        });
        fetchBlogData(currentPage, true);
        return view;
    }

    private synchronized void fetchBlogData(int page, boolean isRefresh) {
        isLoading = true;

        new Thread(() -> {
            List<Blog> newBlogList = blogRepository.fetchBlogData(getContext(), getString(R.string.server_url), page);

            synchronized (blogList) { // 保证对 blogList 的线程安全操作
                if (isRefresh) {
                    blogList.clear();
                }
                blogList.addAll(newBlogList);
            }

            requireActivity().runOnUiThread(() -> {
                if (isRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                adapter.notifyDataSetChanged();
                isLoading = false;

                if (newBlogList.isEmpty()) {
                    Log.d("Whatshot Fragment", "没有更多数据");
                }
            });
        }).start();
    }
}