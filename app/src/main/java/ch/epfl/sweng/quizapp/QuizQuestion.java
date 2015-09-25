/**
 * Created by Gautier on 25/09/2015.
 */
package ch.epfl.sweng.quizapp;
        import java.util.ArrayList;
        import java.util.List;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

/**
 * Encapsulates the data in a quiz question returned by the SwEng server.
 *
 */
public class QuizQuestion {

    protected long ID;
    protected String Owner;
    protected String Question;
    protected ArrayList<String> Answers;
    protected int SolutionIndex;
    protected ArrayList<String> Tags;
    /**
     * Creates a new QuizQuestion instance from the question elements provided
     * as arguments.
     * @param id the numeric ID of the question
     * @param owner the name of the owner of the question
     * @param body the question body
     * @param answers a list of two or more possible question answers
     * @param solutionIndex the index in the answer list of the correct answer
     * @param tags a list of zero or more tags associated to the question
     */
    public QuizQuestion(long id, String owner, String body, List<String> answers,
                        int solutionIndex, List<String> tags) {
        // TODO
        ID = id;
        Owner = owner;
        Question = body;
        Answers.addAll(answers);
        SolutionIndex = solutionIndex;
        Tags.addAll(tags);
    }

    /**
     * Returns the question ID.
     */
    public long getID() {
        return ID;
    }

    /**
     * Returns the question owner.
     */
    public String getOwner() {
        return Owner;
    }

    /**
     * Returns the question body.
     */
    public String getBody() {
        return Question;
    }

    /**
     * Returns a list of the question answers.
     */
    public List<String> getAnswers() {
        return Answers;
    }

    /**
     * Returns the index of the solution in the answer list.
     */
    public int getSolutionIndex() {
        return SolutionIndex;
    }

    /**
     * Returns a (possibly empty) list of question tags.
     */
    public List<String> getTags() {
        return Tags;
    }

    /**
     * Creates a new QuizQuestion object by parsing a JSON object in the format
     * returned by the quiz server.
     * @param jsonObject a {@link JSONObject} encoding.
     * @return a new QuizQuestion object.
     * @throws JSONException in case of malformed JSON.
     */
    public static QuizQuestion parseFromJSON(JSONObject jsonObject) throws JSONException {

        long tempID = jsonObject.getLong("id");
        String tempOwner = jsonObject.getString("owner");
        String tempBody = jsonObject.getString("question");

        JSONArray jsonAnswers = jsonObject.getJSONArray("answers");
        ArrayList<String> tempAnswers = new ArrayList<>();

        for (int i = 0; i < jsonAnswers.length(); i++){
            tempAnswers.add(jsonAnswers.getString(i));
        }

        int tempSolutionIndex = jsonObject.getInt("solutionIndex");

        JSONArray jsonTags = jsonObject.getJSONArray("tags");
        ArrayList<String> tempTags = new ArrayList<>();

        for (int i=0; i < jsonTags.length(); i++){
            tempTags.add(jsonTags.getString(i));
        }

        return new QuizQuestion(tempID, tempOwner, tempBody, tempAnswers,
                tempSolutionIndex, tempTags);
    }
}