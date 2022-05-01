# Test merge (Basics)
I definitions.inc
> init
<<<
> merge
Incorrect operands.
<<<
> merge blablabla
A branch with that name does not exist.
<<<
> merge master
Cannot merge a branch with itself.
<<<
> branch splitRoot
<<<
> checkout splitRoot
<<<
+ wug.txt wug.txt
> add wug.txt
<<<
> merge master
There is an untracked file in the way; delete it, or add and commit it first.
<<<
> commit "added wug"
<<<
> rm wug.txt
<<<
> merge master
You have uncommitted changes.
<<<
> add wug.txt
<<<
> merge master
Given branch is an ancestor of the current branch.
<<<
> checkout master
<<<
> merge splitRoot
Current branch fast-forwarded.
<<<
> log
===
${COMMIT_HEAD}
added wug

===
${COMMIT_HEAD}
initial commit

<<<*
