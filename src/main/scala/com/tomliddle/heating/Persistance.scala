package com.tomliddle.heating

import cats.Monad
import cats.effect.{Ref, Sync}
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps

trait Persistance[F[_], K, V] {

  def save(key: K, elem: V): F[Unit]

  def get(idx: K): F[Option[V]]
}


case class Store[I, T](elems: Map[I, T])

case class InMemoryPersistance[F[_]: Sync, K, V](ref: Ref[F, Store[K, V]]) extends Persistance[F, K, V] {

  override def save(key: K, elem: V): F[Unit] = ref.update(f => Store(f.elems.updated(key, elem)))

  override def get(key: K): F[Option[V]] = for {
    r <- ref.get
    res = r.elems.get(key)
  } yield res
}
