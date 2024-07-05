package com.consensys.merkletree.tree;

import com.consensys.merkletree.util.HashUtil;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MerkleNode {
    private final String hash;
    private MerkleNode left;
    private MerkleNode right;

    public MerkleNode(String hash) {
        this.hash = hash;
    }

    public MerkleNode(MerkleNode left, MerkleNode right) throws NoSuchAlgorithmException {
        this.left = left;
        this.right = right;
        this.hash = HashUtil.hash(List.of(left.getHash(), right.getHash()));
    }

    public String getHash() {
        return hash;
    }

    public MerkleNode getLeft() {
        return left;
    }

    public MerkleNode getRight() {
        return right;
    }
}
