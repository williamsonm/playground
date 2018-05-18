module Algebra where

-- import Data.Monoid (mconcat)

-- https://stackoverflow.com/questions/48488021/once-i-have-an-f-algebra-can-i-define-foldable-and-traversable-in-terms-of-it/48503929?stw=2#48503929

data Expr a = Branch [a] | Leaf Int

instance Functor Expr where
  fmap f (Branch xs) = Branch (fmap f xs)
  fmap _ (Leaf i) = Leaf i

newtype Fix a = Fix { unFix :: a (Fix a) }

branch :: [Fix Expr] -> Fix Expr
branch = Fix . Branch

leaf :: Int -> Fix Expr
leaf = Fix . Leaf

evalSum :: Expr Int -> Int
evalSum (Branch xs) = sum xs
evalSum (Leaf i) = i

cata :: Functor f => (f b -> b) -> Fix f -> b
cata f = f . fmap (cata f) . unFix

-- cata evalSum $ branch [branch [leaf 1, leaf 2], leaf 3]
-- 6

data Expr2 e a = Branch2 [a] | Leaf2 e

newtype Ex e = Ex { unEx :: Fix (Expr2 e) }

-- evalM :: Monoid m => (e -> m) -> Algebra (Expr e) m
evalM _ (Branch xs) = mconcat xs
evalM f (Leaf i) = f i
