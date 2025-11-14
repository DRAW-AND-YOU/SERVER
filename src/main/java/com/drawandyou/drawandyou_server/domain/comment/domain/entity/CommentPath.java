package com.drawandyou.drawandyou_server.domain.comment.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommentPath {

    private String path;

    private static final String CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int DEPTH_CHUNK_SIZE = 5; // 1depth 당 크기 5
    private static final int MAX_DEPTH = 5; // 댓글 depth 는 최대 5로 가정

    // MIN_CHUNK = "00000"
    private static final String MIN_CHUNK = String.valueOf(CHARSET.charAt(0)).repeat(DEPTH_CHUNK_SIZE);

    // MAX_CHUNK = "zzzzz"
    private static final String MAX_CHUNK = String.valueOf(CHARSET.charAt(CHARSET.length() -1)).repeat(DEPTH_CHUNK_SIZE);

    public static CommentPath create(String path){
        if (isDepthOverflowed(path)){
            throw new IllegalArgumentException("depth overflowed");
        }
        return CommentPath.builder()
                .path(path)
                .build();
    }

    private static boolean isDepthOverflowed(String path) {
        return calculateDepth(path) > MAX_DEPTH;
    }

    private static int calculateDepth(String path){
        // path 길이가 25이면, 이건 25 /5 니까 5depth 겠지.
        return path.length() / DEPTH_CHUNK_SIZE;
    }

    public int getDepth(){
        return calculateDepth(path);
    }

    public boolean isRoot(){
        return calculateDepth(path) == 1;
    }

    public String getParentPath(){
        // 00000 00000 이라면, parent 는 00000 . 끝에 5개 짤라내기.
        return path.substring(0, path.length() - DEPTH_CHUNK_SIZE);
    }

    public CommentPath createChildCommentPath(String descendantsTopPath){
        // 하위 댓글이 처음으로 생성되는 상황
        if (descendantsTopPath == null){
            return CommentPath.create(path + MIN_CHUNK); // 00000 붙이기
        }

        String childrenTopPath = findChildrenTopPath(descendantsTopPath);
        return CommentPath.create(increase(childrenTopPath));

    }


    private String findChildrenTopPath(String descendantsTopPath) {
        return descendantsTopPath.substring(0, (getDepth() + 1) * DEPTH_CHUNK_SIZE);
    }

    private String increase(String childrenTopPath) {
        // 00000 00000 끝에 있는 다섯개를 짤라내서, 1를 더한다.
        String lastChunk = childrenTopPath.substring(childrenTopPath.length() - DEPTH_CHUNK_SIZE);
        if (isChunkOverflowed(lastChunk)){
            throw new IllegalArgumentException("chunk overflowed");
        }
        int charsetLength = CHARSET.length();

        int value = 0;
        for (char ch : lastChunk.toCharArray()){
            value = value * charsetLength + CHARSET.indexOf(ch); // value 는 10진수임
        }

        value = value + 1;

        String result = "";
        for (int i = 0; i < DEPTH_CHUNK_SIZE; i++){
            result = CHARSET.charAt(value % charsetLength) + result;
            value /= charsetLength;
        }

        return childrenTopPath.substring(0, childrenTopPath.length() - DEPTH_CHUNK_SIZE) + result;
        // 더하는 result 는 last chunk 에서 1을 더한 값이다.
    }

    private boolean isChunkOverflowed(String lastChunk) {
        return MAX_CHUNK.equals(lastChunk);
    }

}
