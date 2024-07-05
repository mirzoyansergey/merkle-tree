package com.consensys.merkletree;

import com.consensys.merkletree.tree.MerkleNode;
import com.consensys.merkletree.tree.MerkleProof;
import com.consensys.merkletree.tree.MerkleTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MerkleTreeTest {

    private MerkleTree merkleTree;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        List<String> initialLeaves = Arrays.asList("leaf1", "leaf2", "leaf3");
        merkleTree = new MerkleTree(initialLeaves);
    }

    @Test
    void testTreeConstruction() {
        assertNotNull(merkleTree.getRoot());
    }

    @Test
    void testEmptyTree() throws NoSuchAlgorithmException {
        merkleTree = new MerkleTree(List.of());
        assertNull(merkleTree.getRoot());
        assertNull(merkleTree.generateMerkleProof("leaf1"));
    }

    @Test
    void testInsertLeaf() throws NoSuchAlgorithmException {
        MerkleNode rootBeforeInsert = merkleTree.getRoot();
        String newLeaf = "leaf4";
        merkleTree.insertLeaf(newLeaf);
        assertNotEquals(rootBeforeInsert, merkleTree.getRoot(), "Inserting a new leaf should change the root");

        rootBeforeInsert = merkleTree.getRoot();
        merkleTree.insertLeaf(newLeaf);
        assertEquals(rootBeforeInsert, merkleTree.getRoot(), "Inserting the same leaf should not change the root");
    }

    @Test
    void testUpdateLeaf() throws NoSuchAlgorithmException {
        String oldLeaf = "leaf2";
        String newLeaf = "leaf2New";

        MerkleNode rootBeforeInsert = merkleTree.getRoot();
        merkleTree.updateLeaf(oldLeaf, newLeaf);
        assertNotEquals(rootBeforeInsert, merkleTree.getRoot(), "Updating a leaf should change the root");

        rootBeforeInsert = merkleTree.getRoot();
        merkleTree.updateLeaf(oldLeaf, newLeaf);
        assertEquals(rootBeforeInsert, merkleTree.getRoot(), "Updating a leaf that does not exist should not change the root");
    }

    @Test
    void testGenerateMerkleProof() throws NoSuchAlgorithmException {
        String leafToProof = "leaf1";
        MerkleProof proof = merkleTree.generateMerkleProof(leafToProof);

        assertNotNull(proof);
        assertEquals(leafToProof, proof.getLeaf());
        assertFalse(proof.getProof().isEmpty());

        assertNull(merkleTree.generateMerkleProof("leaf111111"));
    }

    @Test
    void testVerifyValidMerkleProof() throws NoSuchAlgorithmException {
        String leafToProof = "leaf2";
        MerkleProof proof = merkleTree.generateMerkleProof(leafToProof);

        assertTrue(merkleTree.verifyMerkleProof(proof));

        proof.getProof().set(0, "invalidHash");
        assertFalse(merkleTree.verifyMerkleProof(proof));
    }
}
