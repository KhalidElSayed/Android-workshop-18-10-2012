package uma.emilsjolander.uppgift1;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MultipleChoiceQuestionFragment extends Fragment implements OnClickListener{
	
	public interface MultipleChoiceQuestionFragmentListener{
		void onQuestionAnswered(MultipleChoiceQuestionFragment f, MultipleChoiceQuestion mcq, boolean correctAnswer);
	}
	
	private MultipleChoiceQuestion mcq;
	private MultipleChoiceQuestionFragmentListener listener;
	private Button answer1;
	private Button answer2;
	private Button answer3;
	private TextView question;
	
	public static MultipleChoiceQuestionFragment newInstance(MultipleChoiceQuestion mcq, MultipleChoiceQuestionFragmentListener listener){
		MultipleChoiceQuestionFragment frag = new MultipleChoiceQuestionFragment();
		
		frag.mcq = mcq;
		frag.listener = listener;
		
		return frag;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.multiple_choice_question_fragment, null);
		
		question = (TextView) v.findViewById(R.id.question);
		answer1 = (Button) v.findViewById(R.id.answer1);
		answer2 = (Button) v.findViewById(R.id.answer2);
		answer3 = (Button) v.findViewById(R.id.answer3);

		question.setText(mcq.getQuestion());
		answer1.setText(mcq.getAnswers()[0]);
		answer2.setText(mcq.getAnswers()[1]);
		answer3.setText(mcq.getAnswers()[2]);
		
		answer1.setOnClickListener(this);
		answer2.setOnClickListener(this);
		answer3.setOnClickListener(this);
		
		return v;
	}

	@Override
	public void onClick(View v) {
		boolean answeredCorrectly = false;
		if(v == answer1){
			answeredCorrectly = mcq.getCorrectAnswerIndex()==0;
		}else if(v == answer2){
			answeredCorrectly = mcq.getCorrectAnswerIndex()==1;
		}else if(v == answer3){
			answeredCorrectly = mcq.getCorrectAnswerIndex()==2;
		}
		listener.onQuestionAnswered(this, mcq, answeredCorrectly);
		
		answer1.setEnabled(false);
		answer2.setEnabled(false);
		answer3.setEnabled(false);
	}

}
