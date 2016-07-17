{-# LANGUAGE NoImplicitPrelude #-}

module Functor where

import SimplePrelude

class Functor f where
  fmap :: (a -> b) -> f a -> f b

  (<$>) :: (a -> b) -> f a -> f b
  (<$>) = fmap

  (<$) :: a -> f b -> f a
  (<$) = fmap . const

class Functor f => Applicative f where
  pure :: a -> f a

  ap    :: f (a -> b) -> f a -> f b
  (<*>) :: f (a -> b) -> f a -> f b
  (<*>) = ap

  (*>) :: f a -> f b -> f b
  fa *> fb = (id <$ fa) <*> fb

  (<*) :: f a -> f b -> f a
  (<*) = liftA2 const

class Applicative m => Monad m where
  return :: a -> m a
  return = pure

  (>>=) :: m a -> (a -> m b) -> m b

  (>>) :: m a -> m b -> m b
  ma >> mb = ma >>= (\_ -> mb)

liftA2 :: Applicative f => (a -> b -> c) -> f a -> f b -> f c
liftA2 f fa fb = f <$> fa <*> fb
