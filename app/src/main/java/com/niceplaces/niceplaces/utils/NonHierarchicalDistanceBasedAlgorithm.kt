package com.niceplaces.niceplaces.utils

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.algo.Algorithm
import com.google.maps.android.clustering.algo.StaticCluster
import com.google.maps.android.geometry.Bounds
import com.google.maps.android.geometry.Point
import com.google.maps.android.projection.SphericalMercatorProjection
import com.google.maps.android.quadtree.PointQuadTree
import java.util.*

/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ /**
 * A simple clustering algorithm with O(nlog n) performance. Resulting clusters are not
 * hierarchical.
 *
 *
 * High level algorithm:<br></br>
 * 1. Iterate over items in the order they were added (candidate clusters).<br></br>
 * 2. Create a cluster with the center of the item. <br></br>
 * 3. Add all items that are within a certain distance to the cluster. <br></br>
 * 4. Move any items out of an existing cluster if they are closer to another cluster. <br></br>
 * 5. Remove those items from the list of candidate clusters.
 *
 *
 * Clusters have the center of the first element (not the centroid of the items within it).
 */
class NonHierarchicalDistanceBasedAlgorithm<T : ClusterItem?> : Algorithm<T> {
    // @Override
    // @Override
    private var maxDistanceBetweenClusteredItems = DEFAULT_MAX_DISTANCE_AT_ZOOM

    override fun setMaxDistanceBetweenClusteredItems(maxDistance: Int) {
        maxDistanceBetweenClusteredItems = maxDistance
    }

    override fun getMaxDistanceBetweenClusteredItems(): Int {
        return maxDistanceBetweenClusteredItems
    }

    /**
     * Any modifications should be synchronized on mQuadTree.
     */
    private val mItems: MutableCollection<QuadItem<T>> = HashSet()

    /**
     * Any modifications should be synchronized on mQuadTree.
     */
    private val mQuadTree = PointQuadTree<QuadItem<T>>(0.0, 1.0, 0.0, 1.0)
    override fun addItem(item: T): Boolean {
        val quadItem = QuadItem(item)
        synchronized(mQuadTree) {
            mItems.add(quadItem)
            mQuadTree.add(quadItem)
        }
        return true
    }

    override fun addItems(items: Collection<T>): Boolean {
        for (item in items) {
            addItem(item)
        }
        return true
    }

    override fun clearItems() {
        synchronized(mQuadTree) {
            mItems.clear()
            mQuadTree.clear()
        }
    }

    override fun removeItem(item: T): Boolean {
        // QuadItem delegates hashcode() and equals() to its item so,
        //   removing any QuadItem to that item will remove the item
        val quadItem = QuadItem(item)
        synchronized(mQuadTree) {
            mItems.remove(quadItem)
            mQuadTree.remove(quadItem)
        }
        return true
    }

    override fun getClusters(zoom: Float): MutableSet<out Cluster<T>>? {
        val discreteZoom = zoom.toInt()
        val zoomSpecificSpan = maxDistanceBetweenClusteredItems / Math.pow(2.0, discreteZoom.toDouble()) / 256
        val visitedCandidates: MutableSet<QuadItem<T>> = HashSet()
        val results: MutableSet<Cluster<T>> = HashSet()
        val distanceToCluster: MutableMap<QuadItem<T>, Double> = HashMap()
        val itemToCluster: MutableMap<QuadItem<T>, StaticCluster<T>> = HashMap()
        synchronized(mQuadTree) {
            for (candidate in getClusteringItems(mQuadTree, discreteZoom)) {
                if (visitedCandidates.contains(candidate)) {
                    // Candidate is already part of another cluster.
                    continue
                }
                val searchBounds = createBoundsFromSpan(candidate.point, zoomSpecificSpan)
                var clusterItems: Collection<QuadItem<T>>
                clusterItems = mQuadTree.search(searchBounds)
                if (clusterItems.size == 1) {
                    // Only the current marker is in range. Just add the single item to the results.
                    results.add(candidate)
                    visitedCandidates.add(candidate)
                    distanceToCluster[candidate] = 0.0
                    continue
                }
                val cluster = StaticCluster<T>(candidate.mClusterItem!!.position)
                results.add(cluster)
                for (clusterItem in clusterItems) {
                    val existingDistance = distanceToCluster[clusterItem]
                    val distance = distanceSquared(clusterItem.point, candidate.point)
                    if (existingDistance != null) {
                        // Item already belongs to another cluster. Check if it's closer to this cluster.
                        if (existingDistance < distance) {
                            continue
                        }
                        // Move item to the closer cluster.
                        itemToCluster[clusterItem]!!.remove(clusterItem.mClusterItem)
                    }
                    distanceToCluster[clusterItem] = distance
                    cluster.add(clusterItem.mClusterItem)
                    itemToCluster[clusterItem] = cluster
                }
                visitedCandidates.addAll(clusterItems)
            }
        }
        return results
    }

    protected fun getClusteringItems(quadTree: PointQuadTree<QuadItem<T>>?, discreteZoom: Int): Collection<QuadItem<T>> {
        return mItems
    }

    override fun getItems(): Collection<T> {
        val items: MutableList<T> = ArrayList()
        synchronized(mQuadTree) {
            for (quadItem in mItems) {
                items.add(quadItem.mClusterItem)
            }
        }
        return items
    }

    private fun distanceSquared(a: Point, b: Point): Double {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y)
    }

    private fun createBoundsFromSpan(p: Point, span: Double): Bounds {
        // TODO: Use a span that takes into account the visual size of the marker, not just its
        // LatLng.
        val halfSpan = span / 2
        return Bounds(
                p.x - halfSpan, p.x + halfSpan,
                p.y - halfSpan, p.y + halfSpan)
    }

    class QuadItem<T : ClusterItem?>(val mClusterItem: T) : PointQuadTree.Item, Cluster<T> {
        private val mPoint: Point
        private val mPosition: LatLng
        private val singletonSet: Set<T>
        override fun getPoint(): Point {
            return mPoint
        }

        override fun getPosition(): LatLng {
            return mPosition
        }

        override fun getItems(): Set<T> {
            return singletonSet
        }

        override fun getSize(): Int {
            return 1
        }

        override fun hashCode(): Int {
            return mClusterItem.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            return if (other !is QuadItem<*>) {
                false
            } else other.mClusterItem == mClusterItem
        }

        init {
            mPosition = mClusterItem!!.position
            mPoint = PROJECTION.toPoint(mPosition)
            singletonSet = setOf(mClusterItem)
        }
    }

    companion object {
        private const val DEFAULT_MAX_DISTANCE_AT_ZOOM = 100 // essentially 100 dp.
        private val PROJECTION = SphericalMercatorProjection(1.0)
    }

    override fun unlock() {
        TODO("Not yet implemented")
    }

    override fun removeItems(items: MutableCollection<T>?): Boolean {
        TODO("Not yet implemented")
    }

    override fun lock() {
        TODO("Not yet implemented")
    }

    override fun updateItem(item: T): Boolean {
        TODO("Not yet implemented")
    }
}