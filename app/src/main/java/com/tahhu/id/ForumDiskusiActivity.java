package com.tahhu.id;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ForumDiskusiActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private FloatingActionButton fabNewPost;
    private PostAdapter postAdapter;
    private List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_diskusi);

        initializeViews();
        setupRecyclerView();
        setupListeners();
        loadPosts();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        fabNewPost = findViewById(R.id.fabNewPost);
    }

    private void setupRecyclerView() {
        posts = new ArrayList<>();
        postAdapter = new PostAdapter(this, posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void setupListeners() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosts();
            }
        });

        fabNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewPostDialog();
            }
        });
    }

    private void loadPosts() {
        // Simulasi loading posts
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Add sample posts
                posts.clear();
                posts.add(new Post("John Doe", "Pengumuman Kerja Bakti",
                        "Minggu depan akan diadakan kerja bakti...", System.currentTimeMillis()));
                posts.add(new Post("Jane Smith", "Info Pemadaman Listrik",
                        "Besok akan ada pemadaman listrik...", System.currentTimeMillis()));

                postAdapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        }, 1000);
    }

    private void showNewPostDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_new_post, null);
        final EditText titleInput = dialogView.findViewById(R.id.titleInput);
        final EditText contentInput = dialogView.findViewById(R.id.contentInput);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Buat Post Baru")
                .setView(dialogView)
                .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = titleInput.getText().toString();
                        String content = contentInput.getText().toString();
                        if (!title.isEmpty() && !content.isEmpty()) {
                            posts.add(0, new Post("Anda", title, content, System.currentTimeMillis()));
                            postAdapter.notifyItemInserted(0);
                            recyclerView.smoothScrollToPosition(0);
                        }
                    }
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    // Post Model Class
    public static class Post {
        String author;
        String title;
        String content;
        long timestamp;

        Post(String author, String title, String content, long timestamp) {
            this.author = author;
            this.title = title;
            this.content = content;
            this.timestamp = timestamp;
        }
    }

    // Post Adapter Class
    private static class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
        private Context context;
        private List<Post> posts;

        PostAdapter(Context context, List<Post> posts) {
            this.context = context;
            this.posts = posts;
        }

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
            return new PostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PostViewHolder holder, int position) {
            Post post = posts.get(position);
            holder.authorName.setText(post.author);
            holder.postTitle.setText(post.title);
            holder.postContent.setText(post.content);
            holder.postTime.setText(DateUtils.getRelativeTimeSpanString(post.timestamp));
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }

        static class PostViewHolder extends RecyclerView.ViewHolder {
            TextView authorName, postTitle, postContent, postTime;

            PostViewHolder(View itemView) {
                super(itemView);
                authorName = itemView.findViewById(R.id.authorName);
                postTitle = itemView.findViewById(R.id.postTitle);
                postContent = itemView.findViewById(R.id.postContent);
                postTime = itemView.findViewById(R.id.postTime);
            }
        }
    }
}