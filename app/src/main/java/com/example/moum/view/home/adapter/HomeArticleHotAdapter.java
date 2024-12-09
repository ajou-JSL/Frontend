package com.example.moum.view.home.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Article;
import com.example.moum.utils.TimeManager;
import com.example.moum.view.home.HomeFragment;

import java.util.ArrayList;

public class HomeArticleHotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Article> articles;
    private Context context;
    private HomeFragment homeFragment;

    public void setArticles(ArrayList<Article> articles, Context context, HomeFragment homeFragment) {
        this.articles = articles;
        this.context = context;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_article_hot, parent, false);
        return new HomeArticleHotAdapter.HomeArticleHotViewModel(view, context, homeFragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Article article = articles.get(position);
        ((HomeArticleHotAdapter.HomeArticleHotViewModel) holder).bind(article);

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    static class HomeArticleHotViewModel extends RecyclerView.ViewHolder {
        private Article article;
        private TextView articleName;
        private TextView articleAuthor;
        private TextView articleTime;
        private TextView articleViews;
        private ConstraintLayout articleTop;
        private Context context;
        private HomeFragment homeFragment;

        public HomeArticleHotViewModel(@NonNull View itemView, Context context, HomeFragment homeFragment) {
            super(itemView);
            articleName = itemView.findViewById(R.id.article_name);
            articleAuthor = itemView.findViewById(R.id.article_author);
            articleTime = itemView.findViewById(R.id.article_time);
            articleViews = itemView.findViewById(R.id.article_views);
            articleTop = itemView.findViewById(R.id.article_top);
            this.context = context;
            this.homeFragment = homeFragment;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Article article) {
            this.article = article;
            articleName.setText(article.getTitle());
            articleAuthor.setText(article.getAuthor());
            if (article.getCreateAt() != null) {
                articleTime.setText(TimeManager.strToPrettyTime(article.getCreateAt()));
            } else {
                articleTime.setText("");
            }
            articleViews.setText(String.format("%d회", article.getViewCounts()));
            articleTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    homeFragment.onArticleClicked(article.getId(), article.getCategory());
                }
            });
        }
    }
}
