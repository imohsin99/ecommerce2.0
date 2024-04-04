package com.example.commentservice.Controller;

import com.example.Dto.CommentDto;
import com.example.commentservice.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    /**
     * This will return all comments from service
     * @return list of comments in form of data transfer object
     */
    @GetMapping
    public List<CommentDto> getAllComments(){
        return commentService.getAll();
    }

    /**
     * This will return comment based upon id
     * @param id is used to identify comment
     * @return comment matching with id in form of data transfer object
     */
    @GetMapping("/{id}")
    public CommentDto getComment(
            @PathVariable("id") String id
    ){
        return commentService.getCommentById(id);
    }

    /**
     * This will update comment
     * @param id is identifier for which comment to update
     * @param comment is object that will be replaced
     * @return string response based upon successful update
     */
    @PutMapping("/updateComment/{id}")
    public String updateComment(
            @PathVariable("id") String id,
            @RequestBody CommentDto comment
    ){
       commentService.updateComment(id,comment);
       return "Comment Updated";
    }

    /**
     * This will delete comment
     * @param id will identify comment to delete
     * @return string response if comment is deleted
     */
    @DeleteMapping("/{id}")
    public String deleteComment(
            @PathVariable("id") String id
    ){
        commentService.deleteComment(id);
        return "comment deleted";
    }



}
