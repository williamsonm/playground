{-# language MultiParamTypeClasses #-}
{-# language ConstraintKinds #-}
{-# language KindSignatures #-}
{-# language TypeOperators #-}
{-# language RankNTypes #-}
{-# language GADTs #-}

module MonadHom where

import Data.Constraint (Constraint)

data Dict (p :: Constraint) where
  Dict :: p => Dict p

newtype p :- q = Sub (p => Dict q)

foo :: Ord a :- Eq[[[[[a]]]]]
foo = Sub Dict

class Lifting p f where
  lifting :: p a :- p (f a)

class MonadTrans t where
  lift :: Monad m => m a -> t m a
  transform :: Monad m :- Monad (t m)
