package com.example.commentservice.Helper;

import com.example.Models.Comment;
import com.example.Dto.CommentDto;

public class Mapper {
  public static CommentDto CommentToDto(Comment comment){
    CommentDto commentDto = new CommentDto();
    commentDto.setComment_id(comment.getComment_id());
    commentDto.setDescription(comment.getDescription());
    commentDto.setProduct(comment.getProduct());
    commentDto.setUser(comment.getUser());

    return commentDto;
  }
  public static Comment DtoToComment(CommentDto commentDto){
    Comment comment = new Comment();
    comment.setComment_id(commentDto.getComment_id());
    comment.setDescription(commentDto.getDescription());
    comment.setProduct(commentDto.getProduct());
    comment.setUser(commentDto.getUser());
    return comment;
  }
}
