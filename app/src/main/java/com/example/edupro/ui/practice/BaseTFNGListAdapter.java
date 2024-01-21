//package com.example.edupro.ui.practice;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.ViewModel;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.edupro.R;
//import com.example.edupro.model.reading.TFNGQuestion;
//import com.example.edupro.ui.practice.reading.practice.question.MCQClickInterface;
//
//import java.util.ArrayList;
//
//public class TFNGListAdapter<VM extends ViewModel> extends RecyclerView.Adapter<TFNGListAdapter.ViewHolder> {
//    private int index = 0;
//    private final ArrayList<TFNGQuestion> tfngQuestions;
//    private final MCQClickInterface mcqClickInterface;
//    private final VM viewModel;
//
//    public TFNGListAdapter(ArrayList<TFNGQuestion> tfngQuestions, int index, VM viewModel, MCQClickInterface mcqClickInterface) {
//        this.tfngQuestions = tfngQuestions;
//        this.index = index;
//        this.mcqClickInterface = mcqClickInterface;
//        this.viewModel = viewModel;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reading_tfng_question_layout, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        String questionNumber = "# Question " + (this.index + position + 1);
//        holder.questionNumber.setText(questionNumber);
//        String questionContent = tfngQuestions.get(position).getContent();
//        holder.questionContent.setText(questionContent);
//
//        String answer = getAnswerAtIndex(position + index);
//        if (answer != null) {
//            switch (answer) {
//                case "0":
//                    holder.radioGroup.check(R.id.radioButton_reading_true);
//                    break;
//                case "1":
//                    holder.radioGroup.check(R.id.radioButton_reading_false);
//                    break;
//                case "2":
//                    holder.radioGroup.check(R.id.radioButton_reading_notgiven);
//                    break;
//            }
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return tfngQuestions.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView questionNumber;
//        TextView questionContent;
//        RadioGroup radioGroup;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            questionNumber = itemView.findViewById(R.id.reading_tfng_question_number);
//            questionContent = itemView.findViewById(R.id.reading_tfng_question_content);
//            radioGroup = itemView.findViewById(R.id.radioGroup_tfng_question);
//
//            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
//                if (checkedId == R.id.radioButton_reading_true) {
//                    mcqClickInterface.onItemClick(getAdapterPosition(), "0");
//                } else if (checkedId == R.id.radioButton_reading_false) {
//                    mcqClickInterface.onItemClick(getAdapterPosition(), "1");
//                } else if (checkedId == R.id.radioButton_reading_notgiven) {
//                    mcqClickInterface.onItemClick(getAdapterPosition(), "2");
//                }
//            });
//        }
//    }
//
//    private String getAnswerAtIndex(int position) {
//        // Implement this method based on the actual structure of your ViewModel
//        // Example: return ((YourViewModelType) viewModel).getAnswerAtIndex(position).getValue();
//        return null;
//    }
//}
