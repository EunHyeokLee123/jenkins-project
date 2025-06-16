package com.playdata.postservice.post.entity;


import jakarta.persistence.*;
import lombok.*;
import com.playdata.postservice.common.entity.BaseTimeEntity;


@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "tbl_comment")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="Post_id")
    private Post post;

    @JoinColumn
    private Long userId;

    private String content;


}
