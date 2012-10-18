package uma.emilsjolander.uppgift1;

import uma.emilsjolander.uppgift1.MultipleChoiceQuestionFragment.MultipleChoiceQuestionFragmentListener;
import uma.emilsjolander.uppgift1.QuizFinishedFragment.QuizFinishedFragmentListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;

public class MainActivity extends Activity implements MultipleChoiceQuestionFragmentListener, QuizFinishedFragmentListener{
	
	private int numberOfCorrectAnswers = 0;
	private int currentPosition = 0;
	private MultipleChoiceQuestion[] questions;
	private ViewGroup fragmentContainer;
	private View contentView;
	private View postBoxTop;
	private View postBoxBottom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        contentView = (ViewGroup) findViewById(R.id.content_view);
        fragmentContainer = (ViewGroup) findViewById(R.id.fragment_container);
        postBoxTop = findViewById(R.id.post_box_top);
        postBoxBottom = findViewById(R.id.post_box_bottom);
        
        questions = new MultipleChoiceQuestion[]{
        		new MultipleChoiceQuestion("Hur långt bort från jorden ligger månen?", new String[]{"100 m","384,400 Km","500,000 Km"}, 1),
        		new MultipleChoiceQuestion("Hur många planeter finns i vårat solsystem?", new String[]{"8","9","10"}, 0),
        		new MultipleChoiceQuestion("vad blir 5+5*2", new String[]{"20","15","matte?"}, 1),
        		new MultipleChoiceQuestion("Om det finns 100 kakor och Anton käkar 75 stycken, hur många finns då kvar?", new String[]{"2 kakor","1 kaka","25 kakor"}, 2),
        		new MultipleChoiceQuestion("Olika versioner av OSX får namn efter vadå?", new String[]{"katter","hundar","sköldpaddor"}, 0)};
        showFirstCardAnimated();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	// TODO Auto-generated method stub
    	super.onSaveInstanceState(outState);
    }

	@Override
	public void onQuestionAnswered(MultipleChoiceQuestionFragment f, MultipleChoiceQuestion mcq, boolean correctAnswer) {
		currentPosition ++;
		if(correctAnswer) numberOfCorrectAnswers++;
		if(mcq.equals(questions[questions.length-1])){
			hideLastCardAndshowResultAnimated(f);
		}else{
			showNextCardAnimated(f);
		}
	}
	
	public void animateFragmentContainerIn(int delay){
		fragmentContainer.setTranslationY(-getResources().getDisplayMetrics().heightPixels/2);
		fragmentContainer.setTranslationX(getResources().getDisplayMetrics().widthPixels);
		fragmentContainer.setRotation(45);
		fragmentContainer.setAlpha(0);
		fragmentContainer.setScaleX(0.5f);
		fragmentContainer.setScaleY(0.5f);
		fragmentContainer.animate().setDuration(300).alpha(1).scaleX(0.8f).scaleY(0.8f).translationX(0).translationY(0).rotation(0).setStartDelay(delay).setListener(null).start();
	}
	
	public void showFirstCardAnimated(){
		getFragmentManager().beginTransaction().add(fragmentContainer.getId(), MultipleChoiceQuestionFragment.newInstance(questions[currentPosition], this)).commit();
		animateFragmentContainerIn(1000);
	}
	
	public void showNextCardAnimated(final Fragment currentCard){
		fragmentContainer.setAlpha(1);
		fragmentContainer.setScaleX(0.8f);
		fragmentContainer.setScaleY(0.8f);
		ViewPropertyAnimator anim = fragmentContainer.animate().setStartDelay(100).translationY(contentView.getHeight());
		anim.setDuration(500);
		anim.setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				getFragmentManager().beginTransaction().remove(currentCard).add(fragmentContainer.getId(), 
						MultipleChoiceQuestionFragment.newInstance(questions[currentPosition], MainActivity.this)).commit();
				animateFragmentContainerIn(0);
			}
		});
		anim.start();
	}
	
	public void hideLastCardAndshowResultAnimated(final Fragment currentCard){
		fragmentContainer.setAlpha(1);
		fragmentContainer.setScaleX(0.8f);
		fragmentContainer.setScaleY(0.8f);
		ViewPropertyAnimator anim = fragmentContainer.animate().setStartDelay(100).translationY(contentView.getHeight());
		anim.setDuration(400);
		anim.setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				fragmentContainer.setAlpha(1);
				fragmentContainer.setScaleX(1f);
				fragmentContainer.setScaleY(1f);
				fragmentContainer.setTranslationY(contentView.getHeight());
				fragmentContainer.setBackgroundColor(getResources().getColor(R.color.post_box_frag_bg));
				getFragmentManager().beginTransaction().remove(currentCard).add(fragmentContainer.getId(), 
						QuizFinishedFragment.newInstance(questions.length,numberOfCorrectAnswers,MainActivity.this),"QuizFinishedFragment").commit();
				AnimatorSet animSet = new AnimatorSet();
				ObjectAnimator animTop = ObjectAnimator.ofFloat(postBoxTop, View.TRANSLATION_Y, -(contentView.getHeight()-(postBoxTop.getHeight()+postBoxBottom.getHeight())));
				ObjectAnimator animBottom = ObjectAnimator.ofFloat(postBoxBottom, View.TRANSLATION_Y, -(contentView.getHeight()-(postBoxTop.getHeight()+postBoxBottom.getHeight())));
				ObjectAnimator animFrag = ObjectAnimator.ofFloat(fragmentContainer, View.TRANSLATION_Y, postBoxTop.getHeight()+postBoxBottom.getHeight());
				animSet.playTogether(animTop,animBottom,animFrag);
				animSet.setDuration(600);
				animSet.start();
			}
		});
		anim.start();
	}
	
	public void restartQuizFromResultStateAnimated(){
		currentPosition = 0;
		numberOfCorrectAnswers = 0;
		AnimatorSet animSet = new AnimatorSet();
		ObjectAnimator animTop = ObjectAnimator.ofFloat(postBoxTop, View.TRANSLATION_Y, 0);
		ObjectAnimator animBottom = ObjectAnimator.ofFloat(postBoxBottom, View.TRANSLATION_Y, 0);
		ObjectAnimator animFrag = ObjectAnimator.ofFloat(fragmentContainer, View.TRANSLATION_Y, contentView.getHeight());
		animSet.playTogether(animTop,animBottom,animFrag);
		animSet.setDuration(600);
		animSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				fragmentContainer.setTranslationY(0);
				fragmentContainer.setBackgroundColor(Color.TRANSPARENT);
				FragmentManager fm = getFragmentManager();
				fm.beginTransaction().remove(fm.findFragmentByTag("QuizFinishedFragment")).commitAllowingStateLoss();

				//TODO throw out all the cards
				showFirstCardAnimated();
			}
		});
		animSet.start();
	}

	@Override
	public void restartRequested() {
		restartQuizFromResultStateAnimated();
	}
    
}