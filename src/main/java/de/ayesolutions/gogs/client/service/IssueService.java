package de.ayesolutions.gogs.client.service;

import de.ayesolutions.gogs.client.GogsClient;
import de.ayesolutions.gogs.client.model.Comment;
import de.ayesolutions.gogs.client.model.Issue;
import de.ayesolutions.gogs.client.model.IssueLabel;
import de.ayesolutions.gogs.client.model.Milestone;

import javax.ws.rs.core.GenericType;
import java.util.Collections;
import java.util.List;

/**
 * service class for issue management.
 *
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class IssueService extends BaseService {

    /**
     * default constructor.
     *
     * @param client gogs http client.
     */
    public IssueService(final GogsClient client) {
        super(client);
    }

    /**
     * list alle issues for specified repository.
     * <p>
     * GET /api/v1/repos/:username/:reponame/issues
     *
     * @param username       username. username.
     * @param repositoryName repository name. repository name.
     * @return list of issues.
     */
    public List<Issue> listIssues(String username, String repositoryName) {
        List<Issue> list = getClient().get(new GenericType<List<Issue>>() {
        }, "repos", username, repositoryName, "issues");
        return list != null ? list : Collections.emptyList();
    }

    /**
     * create new issue to specified repository.
     * <p>
     * POST /api/v1/repos/:username/:reponame/issues
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param issue          issue
     * @return created issue.
     */
    public Issue createIssues(String username, String repositoryName, Issue issue) {
        return getClient().post(Issue.class, issue, "repos", username, repositoryName, "issues");
    }

    /**
     * get specified issue from repository.
     * <p>
     * GET /api/v1/repos/:username/:reponame/issues/:issueid
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param issueId        issue id.
     * @return requested issue.
     */
    public Issue getIssue(String username, String repositoryName, String issueId) {
        return getClient().get(Issue.class, "repos", username, repositoryName, "issues", issueId);
    }

    /**
     * update issue data.
     * <p>
     * PATCH /api/v1/repos/:username/:reponame/issues/:issueId
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param issue          issue.
     * @return updated issue.
     */
    public Issue updateIssue(String username, String repositoryName, Issue issue) {
        return getClient().patch(Issue.class, issue, "repos", username, repositoryName, "issues",
                issue.getId().toString());
    }

    /**
     * get list of comments from an issue.
     * <p>
     * GET /api/v1/repos/:username/:reponame/issues/:issueId/comments
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param issueId        issue id.
     * @return list of comments.
     */
    public List<Comment> listComments(String username, String repositoryName, String issueId) {
        List<Comment> list = getClient().get(new GenericType<List<Comment>>() {
        }, "repos", username, repositoryName, "issues", issueId, "comments");
        return list != null ? list : Collections.emptyList();
    }

    /**
     * create new comment to an issue.
     * <p>
     * POST /api/v1/repos/:username/:reponame/issues/:issueId/comments
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param issueId        issue id.
     * @param comment        comment.
     * @return created comment.
     */
    public Comment createComment(String username, String repositoryName, String issueId, Comment comment) {
        return getClient().post(Comment.class, comment, "repos", username, repositoryName, "issues", issueId,
                "comments");
    }

    /**
     * change comment in issue.
     * <p>
     * PATCH /api/v1/repos/:username/:reponame/issues/:issueId/comments/:commentId
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param issueId        issue id.
     * @param comment        comment
     * @return updated comment.
     */
    public Comment updateComment(String username, String repositoryName, String issueId, Comment comment) {
        return getClient().patch(Comment.class, comment, "repos", username, repositoryName, "issues", issueId,
                "comments", comment.getId().toString());
    }

    /**
     * list all labels associated to issue.
     * <p>
     * GET /api/v1/repos/:username/:reponame/issues/:issueId/labels
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param issueId        issue id.
     * @return list of issue labels.
     */
    public List<IssueLabel> listIssueLabels(String username, String repositoryName, String issueId) {
        List<IssueLabel> list = getClient().get(new GenericType<List<IssueLabel>>() {
        }, "repos", username, repositoryName, "issues", issueId, "labels");
        return list != null ? list : Collections.emptyList();
    }

    /**
     * add issue labels to an issue.
     * <p>
     * POST /api/v1/repos/:username/:reponame/issues/:issueId/labels
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param issueId        issue id.
     * @param issueLabelList list of issue labels.
     * @return added issue labels.
     */
    public List<IssueLabel> addIssueLabels(String username, String repositoryName, String issueId,
                                           List<IssueLabel> issueLabelList) {
        List<IssueLabel> list = getClient().post(new GenericType<List<IssueLabel>>() {
        }, issueLabelList, "repos", username, repositoryName, "issues", issueId, "labels");
        return list != null ? list : Collections.emptyList();
    }

    /**
     * replace all issue labels in an issue.
     * <p>
     * PUT /api/v1/repos/:username/:reponame/issues/:issueId/labels
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param issueId        issue id.
     * @param issueLabelList list of issue labels.
     * @return replaced issue labels.
     */
    public List<IssueLabel> replaceIssueLabels(String username, String repositoryName, String issueId,
                                               List<IssueLabel> issueLabelList) {
        List<IssueLabel> list = getClient().put(new GenericType<List<IssueLabel>>() {
        }, issueLabelList, "repos", username, repositoryName, "issues", issueId, "labels");
        return list != null ? list : Collections.emptyList();
    }

    /**
     * delete all issue labels from an issue.
     * <p>
     * DELETE /api/v1/repos/:username/:reponame/issues/:issueId/labels
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param issueId        issue id.
     */
    public void deleteIssueLabels(String username, String repositoryName, String issueId) {
        getClient().delete(Void.class, "repos", username, repositoryName, "issues", issueId, "labels");
    }

    /**
     * get all configured labels from repository.
     * <p>
     * GET /api/v1/repos/:username/:reponame/labels
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @return list of issue labels.
     */
    public List<IssueLabel> getIssueLabels(String username, String repositoryName) {
        List<IssueLabel> list = getClient().get(new GenericType<List<IssueLabel>>() {
        }, username, repositoryName, "labels");
        return list != null ? list : Collections.emptyList();
    }

    /**
     * add new issue label to repository.
     * <p>
     * POST /api/v1/repos/:username/:reponame/labels
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param issueLabel     issue label.
     * @return added issue label.
     */
    public IssueLabel addIssueLabel(String username, String repositoryName, IssueLabel issueLabel) {
        return getClient().post(IssueLabel.class, issueLabel, "repos", username, repositoryName, "labels");
    }

    /**
     * update existing issue label in repository.
     * <p>
     * PATCH /api/v1/repos/:username/:reponame/labels/:labelId
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param issueLabel     issue label.
     * @return updated issue label.
     */
    public IssueLabel updateIssueLabel(String username, String repositoryName, IssueLabel issueLabel) {
        return getClient().post(IssueLabel.class, issueLabel, "repos", username, repositoryName, "labels",
                issueLabel.getId().toString());
    }

    /**
     * get specified issue label from repository.
     * <p>
     * GET /api/v1/repos/:username/:reponame/labels/:labelId
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param issueLabelId   issue label id.
     * @return issue label.
     */
    public IssueLabel getIssueLabel(String username, String repositoryName, String issueLabelId) {
        return getClient().get(IssueLabel.class, "repos", username, repositoryName, "labels", issueLabelId);
    }

    /**
     * delete specified issue label from repository.
     * <p>
     * DELETE /api/v1/repos/:username/:reponame/labels/:labelId
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param issueLabelId   issue label id.
     */
    public void deleteIssueLabel(String username, String repositoryName, String issueLabelId) {
        getClient().delete(Void.class, "repos", username, repositoryName, "labels", issueLabelId);
    }

    /**
     * get all milestones from repository.
     * <p>
     * GET /api/v1/repos/:username/:reponame/milestones
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @return list of milestones.
     */
    public List<Milestone> getMilestones(String username, String repositoryName) {
        List<Milestone> list = getClient().get(new GenericType<List<Milestone>>() {
        }, username, repositoryName, "milestones");
        return list != null ? list : Collections.emptyList();
    }

    /**
     * add new milestone to repository.
     * <p>
     * POST /api/v1/repos/:username/:reponame/milestones
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param milestone      milestone.
     * @return added milestone.
     */
    public Milestone addMilestone(String username, String repositoryName, Milestone milestone) {
        return getClient().post(Milestone.class, milestone, "repos", username, repositoryName, "milestones");
    }

    /**
     * update existing milestone in repository.
     * <p>
     * PATCH /api/v1/repos/:username/:reponame/milestones/:milestoneId
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param milestone      milestone.
     * @return updated milestone.
     */
    public Milestone updateMilestone(String username, String repositoryName, Milestone milestone) {
        return getClient().patch(Milestone.class, milestone, "repos", username, repositoryName, "milestones",
                milestone.getId().toString());
    }

    /**
     * get specified milestone from repository.
     * <p>
     * GET /api/v1/repos/:username/:reponame/milestones/:milestoneId
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param milestoneId    milestone.
     * @return milestone.
     */
    public Milestone getMilestone(String username, String repositoryName, String milestoneId) {
        return getClient().get(Milestone.class, "repos", username, repositoryName, "milestones", milestoneId);
    }

    /**
     * delete specified milestone from repository.
     * <p>
     * DELETE /api/v1/repos/:username/:reponame/milestones/:milestoneId
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param milestoneId    milestone.
     */
    public void deleteMilestone(String username, String repositoryName, String milestoneId) {
        getClient().delete(Milestone.class, "repos", username, repositoryName, "milestones", milestoneId);
    }
}
