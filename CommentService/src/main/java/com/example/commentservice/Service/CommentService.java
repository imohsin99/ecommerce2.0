package com.example.commentservice.Service;

import com.example.Dto.CommentDto;
import com.example.Models.Comment;
import com.example.commentservice.Exceptions.ServiceException;
import com.example.commentservice.Helper.Mapper;
import com.example.commentservice.Repositoy.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    CommentRepo commentRepo;
    /**
     * This will return all comments from service
     * @return list of comments in form of data transfer object
     */
    public List<CommentDto> getAll() {
        return commentRepo.findAll().stream().map(Mapper::CommentToDto).collect(Collectors.toList());
    }
    /**
     * This will return comment based upon id
     * @param id is used to identify comment
     * @return comment matching with id in form of data transfer object
     */
    public CommentDto getCommentById(String id) {
        return Mapper.CommentToDto(commentRepo.findById(Long.valueOf(id)).get());
    }
    /**
     * This will update comment
     * @param id is identifier for which comment to update
     * @param commentDto is object that will be replaced
     */
    public void updateComment(String id, CommentDto commentDto) {
        Comment comment = Mapper.DtoToComment(commentDto);
        if(comment.getDescription().trim().equals("")){
            throw new ServiceException("Null comments not allowed");
        }
        Comment existComment = commentRepo.findById(Long.valueOf(id)).get();
        existComment.setDescription(comment.getDescription());
        commentRepo.save(existComment);
    }
    /**
     * This will delete comment
     * @param id will identify comment to delete
     * @return string response if comment is deleted
     */
    public void deleteComment(String id) {
        Comment comment = commentRepo.findById(Long.valueOf(id)).get();

        commentRepo.deleteById(comment.getComment_id());
    }


}
