package com.example.demo.dal.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "code_commit")
public class CodeCommit {

    @Id
    private String id;

    @Field("type")
    private String source;

    @Field("repo_name")
    private String repoName;

    @Field("branch")
    private String branch;

    @Field("commit_msg")
    private String commitMsg;

    @Field("author")
    private String author;

    @Field("commit_time")
    private Date commitTime;


}
