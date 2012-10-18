package uma.emilsjolander.uppgift1;

public class MultipleChoiceQuestion {
	
	private String question;
	private String[] answers;
	private int correctAnswerIndex;
	
	public MultipleChoiceQuestion(String question, String[] answers, int correctAnswerIndex) {
		this.question = question;
		this.answers = answers;
		this.correctAnswerIndex = correctAnswerIndex;
	}

	public String getQuestion() {
		return question;
	}
	
	public String[] getAnswers() {
		return answers;
	}

	public int getCorrectAnswerIndex() {
		return correctAnswerIndex;
	}

}
