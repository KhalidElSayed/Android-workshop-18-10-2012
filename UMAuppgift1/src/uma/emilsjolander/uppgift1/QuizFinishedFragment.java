package uma.emilsjolander.uppgift1;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class QuizFinishedFragment extends Fragment implements OnClickListener{
	
	public interface QuizFinishedFragmentListener{
		void restartRequested();
	}
	
	public static QuizFinishedFragment newInstance(int totalNumberOfQuestions, int numberOfCorrectAnswers, QuizFinishedFragmentListener listener){
		QuizFinishedFragment f = new QuizFinishedFragment();
		
		f.totalNumberOfQuestions = totalNumberOfQuestions;
		f.numberOfCorrectAnswers = numberOfCorrectAnswers;
		f.listener = listener;
		
		return f;
	}

	private int totalNumberOfQuestions;
	private int numberOfCorrectAnswers;
	private QuizFinishedFragmentListener listener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.quiz_finished_fragment, null);
		
		final Button b = (Button) v.findViewById(R.id.restart);
		final TextView numberOfQuestionsTextView = (TextView) v.findViewById(R.id.total);
		final TextView numberOfCorrectAnswersTextView = (TextView) v.findViewById(R.id.correct);
		final TextView numberOfIncorrectAnswersTextView = (TextView) v.findViewById(R.id.incorrect);
		final TextView percentageCorrectTextView = (TextView) v.findViewById(R.id.percent);
		
		numberOfQuestionsTextView.setText(getString(R.string.out_of_x_questions_you_got, totalNumberOfQuestions));
		numberOfCorrectAnswersTextView.setText(getString(R.string.x_correct_answers, numberOfCorrectAnswers));
		numberOfIncorrectAnswersTextView.setText(getString(R.string.x_incorrect_answers, totalNumberOfQuestions-numberOfCorrectAnswers));
		
		percentageCorrectTextView.setText(getString(R.string.grade_x, Math.round(((float)numberOfCorrectAnswers/(float)totalNumberOfQuestions)*100))+"%");
		
		b.setOnClickListener(this);
		
		return v;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.restart:
			listener.restartRequested();
			break;
		}
	}

}
