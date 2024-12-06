package com.example.moum.view.myinfo.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.ReportArticle;
import com.example.moum.data.entity.ReportMember;
import com.example.moum.data.entity.ReportTeam;
import com.example.moum.view.myinfo.MyInfoReportNQuestionListActivity;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ReportMember> reportMembers;
    private ArrayList<ReportTeam> reportTeams;
    private ArrayList<ReportArticle> reportArticles;
    private String reportType;
    private Context context;
    private static final int VIEW_TYPE_MEMBER = 1;
    private static final int VIEW_TYPE_TEAM = 2;
    private static final int VIEW_TYPE_ARTICLE = 2;

    public void setReportMembers(ArrayList<ReportMember> reportMembers, String reportType, Context context) {
        this.reportMembers = reportMembers;
        this.reportType = reportType;
        this.context = context;
    }

    public void setReportTeams(ArrayList<ReportTeam> reportTeams, String reportType, Context context) {
        this.reportTeams = reportTeams;
        this.reportType = reportType;
        this.context = context;
    }

    public void setReportArticles(ArrayList<ReportArticle> reportArticles, String reportType, Context context) {
        this.reportArticles = reportArticles;
        this.reportType = reportType;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (reportType.equals("member")) {
            return VIEW_TYPE_MEMBER;
        } else if (reportType.equals("team")) {
            return VIEW_TYPE_TEAM;
        } else {
            return VIEW_TYPE_ARTICLE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MEMBER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
            return new ReportAdapter.ReportMemberViewHolder(view, context);
        } else if (viewType == VIEW_TYPE_TEAM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
            return new ReportAdapter.ReportTeamViewHolder(view, context);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
            return new ReportAdapter.ReportArticleViewHolder(view, context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReportAdapter.ReportMemberViewHolder) {
            ReportMember reportMember = reportMembers.get(position);
            ((ReportAdapter.ReportMemberViewHolder) holder).bind(reportMember);
        } else if (holder instanceof ReportAdapter.ReportTeamViewHolder) {
            ReportTeam reportTeam = reportTeams.get(position);
            ((ReportAdapter.ReportTeamViewHolder) holder).bind(reportTeam);
        } else if (holder instanceof ReportAdapter.ReportArticleViewHolder) {
            ReportArticle reportArticle = reportArticles.get(position);
            ((ReportAdapter.ReportArticleViewHolder) holder).bind(reportArticle);
        }
    }

    @Override
    public int getItemCount() {
        if (reportType.equals("member")) {
            return reportMembers.size();
        } else if (reportType.equals("team")) {
            return reportTeams.size();
        } else {
            return reportArticles.size();
        }
    }

    static class ReportMemberViewHolder extends RecyclerView.ViewHolder {
        private ReportMember reportMember;
        private TextView reportType;
        private TextView reportState;
        private TextView reportDate;
        private LinearLayout reportTop;
        private Context context;
        private MyInfoReportNQuestionListActivity myInfoReportNQuestionListActivity;

        public ReportMemberViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            reportType = itemView.findViewById(R.id.textview_report_type);
            reportState = itemView.findViewById(R.id.textview_report_state);
            reportDate = itemView.findViewById(R.id.textview_report_date);
            reportTop = itemView.findViewById(R.id.button_report);
            this.context = context;
            this.myInfoReportNQuestionListActivity = (MyInfoReportNQuestionListActivity) context;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(ReportMember reportMember) {
            reportType.setText("멤버 신고");
            if (reportMember.getResolved()) {
                reportState.setText("처리 완료");
            } else {
                reportState.setText("처리중");
            }
            reportTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myInfoReportNQuestionListActivity.onReportMemberClicked(reportMember.getId());
                }
            });
        }
    }

    static class ReportTeamViewHolder extends RecyclerView.ViewHolder {
        private ReportTeam reportTeam;
        private TextView reportType;
        private TextView reportState;
        private TextView reportDate;
        private LinearLayout reportTop;
        private Context context;
        private MyInfoReportNQuestionListActivity myInfoReportNQuestionListActivity;

        public ReportTeamViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            reportType = itemView.findViewById(R.id.textview_report_type);
            reportState = itemView.findViewById(R.id.textview_report_state);
            reportDate = itemView.findViewById(R.id.textview_report_date);
            reportTop = itemView.findViewById(R.id.button_report);
            this.context = context;
            this.myInfoReportNQuestionListActivity = (MyInfoReportNQuestionListActivity) context;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(ReportTeam reportTeam) {
            reportType.setText("단체 신고");
            if (reportTeam.getResolved()) {
                reportState.setText("처리 완료");
            } else {
                reportState.setText("처리중");
            }
            reportTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myInfoReportNQuestionListActivity.onReportTeamClicked(reportTeam.getId());
                }
            });
        }
    }

    static class ReportArticleViewHolder extends RecyclerView.ViewHolder {
        private ReportArticle reportArticle;
        private TextView reportType;
        private TextView reportState;
        private TextView reportDate;
        private LinearLayout reportTop;
        private Context context;
        private MyInfoReportNQuestionListActivity myInfoReportNQuestionListActivity;

        public ReportArticleViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            reportType = itemView.findViewById(R.id.textview_report_type);
            reportState = itemView.findViewById(R.id.textview_report_state);
            reportDate = itemView.findViewById(R.id.textview_report_date);
            reportTop = itemView.findViewById(R.id.button_report);
            this.context = context;
            this.myInfoReportNQuestionListActivity = (MyInfoReportNQuestionListActivity) context;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(ReportArticle reportArticle) {
            reportType.setText("게시글 신고");
            if (reportArticle.getResolved()) {
                reportState.setText("처리 완료");
            } else {
                reportState.setText("처리중");
            }
            reportTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myInfoReportNQuestionListActivity.onReportArticleClicked(reportArticle.getId());
                }
            });
        }
    }
}
