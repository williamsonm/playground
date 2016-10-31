module TailrecM where

tailrecM :: Monad m => (a -> m (Either a b)) -> a -> m b
tailrecM f a = f a >>= ret
  where ret (Right b) = return b
        ret (Left a') = tailrecM f a'

wut :: Monad m => Int -> m (Either Int Int)
wut 1 = return (Right 1)
wut x = return (Left (x - 1))
