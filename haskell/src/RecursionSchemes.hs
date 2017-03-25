{-# LANGUAGE DeriveFunctor #-}

module RecursionSchemes where

import Control.Arrow

-- http://blog.sumtypeofway.com/an-introduction-to-recursion-schemes/

data Lit
  = StrLit String
  | IntLit Int
  | Ident String
  deriving (Show, Eq)

data Expr
  = Index Expr Expr
  | Call Expr [Expr]
  | Unary String Expr
  | Binary Expr String Expr
  | Paren Expr
  | Literal Lit
  deriving (Show, Eq)

data Stmt
  = Break
  | Continue
  | Empty
  | IfElse Expr [Stmt] [Stmt]
  | Return (Maybe Expr)
  | While Expr [Stmt]
  | Expressions Expr
  deriving (Show, Eq)

flatten :: Expr -> Expr
flatten (Literal i) = Literal i
flatten (Paren e) = flatten e
-- all the other cases preserve their constructors and just apply
-- the flatten function to their children that are of type `Expr`.
flatten (Index e i)     = Index (flatten e) (flatten i)
flatten (Call e args)   = Call (flatten e) (map flatten args)
flatten (Unary op arg)  = Unary op (flatten arg)
flatten (Binary l op r) = Binary (flatten l) op (flatten r)

applyExpr :: (Expr -> Expr) -> Expr -> Expr
-- base case: applyExpr is the identity function on constants
applyExpr _ (Literal i) = Literal i

-- recursive cases: apply f to each subexpression
applyExpr f (Paren p)       = Paren (f p)
applyExpr f (Index e i)     = Index (f e) (f i)
applyExpr f (Call e args)   = Call (f e) (map f args)
applyExpr f (Unary op arg)  = Unary op (f arg)
applyExpr f (Binary l op r) = Binary (f l) op (f r)

flatten2 :: Expr -> Expr
flatten2 (Paren e) = flatten2 e
flatten2 x = applyExpr flatten2 x

data Expr' a
  = Index' a a
  | Call' a [a]
  | Unary' String a
  | Binary' a String a
  | Paren' a
  | Literal' Lit
  deriving (Show, Eq, Functor)

data Term f = In (f (Term f))

out :: Term f -> f (Term f)
out (In t) = t

flattenTerm :: Term Expr' -> Term Expr'
flattenTerm (In (Paren' e)) = e
flattenTerm other = other

flatten3 :: Term Expr' -> Term Expr'
flatten3 = bottomUp flattenTerm

topDown, bottomUp :: Functor a => (Term a -> Term a) -> Term a -> Term a
topDown  f = In  <<< fmap (bottomUp f) <<< out <<< f
bottomUp f = out >>> fmap (bottomUp f) >>> In  >>> f
