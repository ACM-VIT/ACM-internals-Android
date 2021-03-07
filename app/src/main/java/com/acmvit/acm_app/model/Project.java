package com.acmvit.acm_app.model;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.Junction;
import androidx.room.Relation;

import com.acmvit.acm_app.db.model.ProjectMemberCrossRef;
import com.acmvit.acm_app.db.model.ProjectTagCrossRef;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Project {

    private static final String TAG = "Project";

    @SerializedName("id")
    private String project_id;

    @SerializedName("status")
    private ProjectStatus status;

    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "desc")
    @SerializedName("desc")
    private String description;

    @Expose(serialize = false, deserialize = false)
    @ColumnInfo(name = "founder_id")
    private String founderId;

    @Relation(
            entity = Tag.class,
            parentColumn = "project_id",
            entityColumn = "tag",
            associateBy = @Junction(ProjectTagCrossRef.class)
    )
    @SerializedName("tags")
    private List<String> tags;

    @SerializedName("icon")
    private String icon;

    @Ignore
    @SerializedName("founder")
    private User founder;

    @Relation(
            parentColumn = "project_id",
            entityColumn = "user_id",
            associateBy = @Junction(ProjectMemberCrossRef.class)
    )
    @Expose(serialize = false, deserialize = false)
    private List<User> members;

    @Ignore
    @SerializedName("teamMembersId")
    private List<String> memberIds;

    @Ignore
    @SerializedName("teamMembersProfilePic")
    private List<String> teamMemberProfilePics;

    @Ignore
    @SerializedName("teamMembers")
    private List<String> teamMemberNames;

    @Ignore
    @SerializedName("updatedAt")
    private Timestamp timestamp;

    public Project() {
    }

    public Project(@NotNull String project_id, ProjectStatus status, String name, String description, List<String> tags, User founder, List<User> members, String icon, List<String> memberIds, List<String> teamMemberProfilePics, List<String> teamMemberNames, Timestamp timestamp) {
        this.project_id = project_id;
        this.status = status;
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.founder = founder;
        this.members = members;
        this.icon = icon;
        this.memberIds = memberIds;
        this.teamMemberProfilePics = teamMemberProfilePics;
        this.teamMemberNames = teamMemberNames;
        this.timestamp = timestamp;
    }

    public Project(String id, ProjectStatus status, String name, String disp) {
        this.project_id = project_id;
        this.status = status;
        this.name = name;
        this.description = description;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }

    public List<String> getTeamMemberProfilePics() {
        return teamMemberProfilePics;
    }

    public void setTeamMemberProfilePics(List<String> teamMemberProfilePics) {
        this.teamMemberProfilePics = teamMemberProfilePics;
    }

    public List<String> getTeamMemberNames() {
        return teamMemberNames;
    }

    public void setTeamMemberNames(List<String> teamMemberNames) {
        this.teamMemberNames = teamMemberNames;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @NotNull
    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(@NotNull String project_id) {
        this.project_id = project_id;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public User getFounder() {
        return founder;
    }

    public void setFounder(User founder) {
        this.founder = founder;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * computes a list with the first item as the founder followed by other members
     */
    public List<User> getMembersWithFounder() {
        List<User> membersWithFounder = new ArrayList<>();

        if(getMembers() != null && !getMembers().isEmpty()) {
            membersWithFounder.addAll(getMembers());
        } else if (memberIds != null){
            for (int i = 0; i < memberIds.size(); i++) {
                User user = new User(memberIds.get(i), teamMemberNames.get(i), teamMemberProfilePics.get(i));
                membersWithFounder.add(user);
            }
        }

        if (getFounder() == null) {
            for (User user : membersWithFounder) {
                if (user.getUser_id().equals(founderId)) {
                    founder = user;
                    break;
                }
            }
        }

        membersWithFounder.remove(getFounder());
        Collections.sort(membersWithFounder, (o1, o2) -> o1.getName().compareTo(o2.getName()));
        membersWithFounder.add(0, getFounder());
        return membersWithFounder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(project_id, project.project_id) &&
                status == project.status &&
                Objects.equals(name, project.name) &&
                Objects.equals(description, project.description) &&
                Objects.equals(tags, project.tags) &&
                Objects.equals(icon, project.icon) &&
                Objects.equals(getMembersWithFounder(), project.getMembersWithFounder());
    }

    @Override
    public int hashCode() {
        return Objects.hash(project_id, status, name, description, tags, icon, founder, members);
    }

    @NotNull
    @Override
    public String toString() {
        return "Project{" +
                "user_id='" + project_id + '\'' +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                ", icon='" + icon + '\'' +
                getMembersWithFounder() +
                '}';
    }

    public String getFounderId() {
        return founderId;
    }

    public void setFounderId(String founderId) {
        this.founderId = founderId;
    }
}
