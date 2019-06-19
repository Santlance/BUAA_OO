import com.oocourse.specs1.models.Path;
import com.oocourse.specs1.models.PathContainer;
import com.oocourse.specs1.models.PathIdNotFoundException;
import com.oocourse.specs1.models.PathNotFoundException;

import java.util.HashMap;
import java.util.Iterator;

public class MyPathContainer implements PathContainer {
    //@ public instance model non_null Path[] pList;
    //@ public instance model non_null int[] pidList;
    private HashMap<Integer, MyPath> pathList;
    //here we set the key as pid and set the value as path
    private HashMap<MyPath, Integer> pathIdList;
    //here we set the key as path and set the value as pid
    private HashMap<Integer, Integer> distinctNode;
    //here we set the key as node and set the value as weigh
    private static int pidcount = 0;

    public MyPathContainer() {
        this.distinctNode = new HashMap<>();
        this.pathIdList = new HashMap<>();
        this.pathList = new HashMap<>();
    }

    //@ ensures \result == pList.length;
    public /*@pure@*/int size() {
        return this.pathList.size();
    }

    /*@ requires path != null;
      @ assignable \nothing;
      @ ensures \result == (\exists int i; 0 <= i && i < pList.length;
      @                     pList[i].equals(path));
      @*/
    public /*@pure@*/ boolean containsPath(Path path) {
        return this.pathIdList.containsKey((MyPath) path);
    }

    /*@ ensures \result == (\exists int i; 0 <= i && i < pidList.length;
      @                      pidList[i] == pathId);
      @*/
    public /*@pure@*/ boolean containsPathId(int pathId) {
        return this.pathList.containsKey(pathId);
    }

    /*@ public normal_behavior
      @ requires containsPathId(pathId);
      @ assignable \nothing;
      @ ensures (pidList.length == pList.length)
      @ && (\exists int i; 0 <= i && i < pList.length;
      @ pidList[i] == pathId && \result == pList[i]);
      @ also
      @ public exceptional_behavior
      @ requires !containsPathId(pathId);
      @ assignable \nothing;
      @ signals_only PathIdNotFoundException;
      @*/
    public /*@pure@*/ Path getPathById(int pathId)
            throws PathIdNotFoundException {
        if (!this.pathList.containsKey(pathId)) {
            throw new PathIdNotFoundException(pathId);
        }
        return this.pathList.get(pathId);
    }

    /*@ public normal_behavior
      @ requires path != null && path.isValid() && containsPath(path);
      @ assignable \nothing;
      @ ensures (pidList.length == pList.length) && (\exists int i;
      @ 0 <= i && i < pList.length; pList[i].equals(path)
      @ && pidList[i] == \result);
      @ also
      @ public exceptional_behavior
      @ signals (PathNotFoundException e) path == null;
      @ signals (PathNotFoundException e) !path.isValid();
      @ signals (PathNotFoundException e) !containsPath(path);
      @*/
    public /*@pure@*/ int getPathId(Path path) throws PathNotFoundException {
        if (path == null || !((MyPath) path).isValid()
                || !this.pathIdList.containsKey(path)) {
            throw new PathNotFoundException(path);
        }
        return this.pathIdList.get(path);
    }

    /*@ normal_behavior
      @ requires path != null && path.isValid();
      @ assignable pList, pidList;
      @ ensures (pidList.length == pList.length);
      @ ensures (\exists int i; 0 <= i && i < pList.length; pList[i] == path &&
      @           \result == pidList[i]);
      @ ensures !\old(containsPath(path)) ==>
      @          pList.length == (\old(pList.length) + 1) &&
      @          pidList.length == (\old(pidList.length) + 1);
      @ ensures (\forall int i; 0 <= i && i < \old(pList.length);
      @          containsPath(\old(pList[i]))
      @          && containsPathId(\old(pidList[i])));
      @ also
      @ normal_behavior
      @ requires path == null || path.isValid() == false;
      @ assignable \nothing;
      @ ensures \result == 0;
      @*/
    public int addPath(Path path) {
        if (path == null || !((MyPath) path).isValid()) {
            return 0;
        }
        if (this.pathIdList.containsKey((MyPath) path)) {
            return this.pathIdList.get(path);
        }
        Iterator<Integer> iteratorP = ((MyPath) path).iterator();
        while (iteratorP.hasNext()) {
            int node = iteratorP.next();
            if (this.distinctNode.containsKey(node)) {
                int value = this.distinctNode.get(node);
                this.distinctNode.replace(node, value + 1);
            } else {
                this.distinctNode.put(node, 1);
            }
        }
        this.pathList.put(++pidcount, (MyPath) path);
        this.pathIdList.put((MyPath) path, pidcount);
        return pidcount;
    }

    /*@ public normal_behavior
      @ requires path != null && path.isValid() && \old(containsPath(path));
      @ assignable pList, pidList;
      @ ensures containsPath(path) == false;
      @ ensures (pidList.length == pList.length);
      @ ensures (\exists int i; 0 <= i &&
      @ i < \old(pList.length); \old(pList[i].equals(path)) &&
      @           \result == \old(pidList[i]));
      @ also
      @ public exceptional_behavior
      @ assignable \nothing;
      @ signals (PathNotFoundException e) path == null;
      @ signals (PathNotFoundException e) path.isValid()==false;
      @ signals (PathNotFoundException e) !containsPath(path);
      @*/
    public int removePath(Path path) throws PathNotFoundException {
        if (path == null || !((MyPath) path).isValid()
                || !this.pathIdList.containsKey((MyPath) path)) {
            throw new PathNotFoundException(path);
        }
        Iterator<Integer> iteratorP = ((MyPath) path).iterator();
        while (iteratorP.hasNext()) {
            int node = iteratorP.next();
            if (this.distinctNode.containsKey(node)) {
                int value = this.distinctNode.get(node);
                if (value == 1) {
                    this.distinctNode.remove(node);
                } else {
                    this.distinctNode.replace(node, value - 1);
                }
            }
        }
        int keyid = this.pathIdList.get(path);
        this.pathList.remove(keyid);
        this.pathIdList.remove(path);
        return keyid;
    }

    /*@ public normal_behavior
      @ requires \old(containsPathId(pathId));
      @ assignable pList, pidList;
      @ ensures pList.length == pidList.length;
      @ ensures (\forall int i; 0 <= i &&
      @ i < pidList.length; pidList[i] != pathId);
      @ ensures (\forall int i; 0 <= i &&
      @ i < pList.length; !pList[i].equals(\old(getPathById(pathId))));
      @ also
      @ public exceptional_behavior
      @ assignable \nothing;
      @ signals (PathIdNotFoundException e) !containsPathId(pathId);
      @*/
    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (!this.containsPathId(pathId)) {
            throw new PathIdNotFoundException(pathId);
        }
        MyPath path = this.pathList.get(pathId);
        Iterator<Integer> iteratorP = ((MyPath) path).iterator();
        while (iteratorP.hasNext()) {
            int node = iteratorP.next();
            if (this.distinctNode.containsKey(node)) {
                int value = this.distinctNode.get(node);
                if (value == 1) {
                    this.distinctNode.remove(node);
                } else {
                    this.distinctNode.replace(node, value - 1);
                }
            }
        }
        MyPath path1 = this.pathList.get(pathId);
        this.pathList.remove(pathId);
        this.pathIdList.remove(path1);
    }

    /*@ ensures (\exists int[] arr;
      @ (\forall int i, j; 0 <= i && i < j && j < arr.length; arr[i] != arr[j]);
      @             (\forall int i; 0 <= i && i < arr.length; (\exists Path p;
      @             this.containsPath(p); p.containsNode(arr[i])))
      @             &&(\forall Path p; this.containsPath(p);
      @             (\forall int node; p.containsNode(node);
      @             (\exists int i; 0 <= i && i < arr.length; node == arr[i])))
      @             &&(\result == arr.length));
      @*/
    public /*@pure@*/int getDistinctNodeCount() {
        return this.distinctNode.size();
    } //在容器全局范围内查找不同的节点数
}
