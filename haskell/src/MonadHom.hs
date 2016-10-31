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

type m ~> n = forall a. m a -> n a

class Lifting Monad t => MFunctor t where
  hoist :: (Monad m, Monad n) => (m ~> n) -> (t m ~> t n)

class MonadTrans t where
  lift :: Monad m => m ~> t m
  transform :: Monad m :- Monad (t m)

class (Lifting Monad t, MonadTrans t) => MPointed t
instance (Lifting Monad t, MonadTrans t) => MPointed t
