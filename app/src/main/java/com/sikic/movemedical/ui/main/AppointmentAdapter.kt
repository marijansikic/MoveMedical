package com.sikic.movemedical.ui.main

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.sikic.movemedical.R
import com.sikic.movemedical.databinding.ItemAppointmentBinding
import com.sikic.movemedical.db.entity.Appointment

class AppointmentAdapter(
    private val adapterListener: AppointmentAdapterListener,
    private val context: Context
) :
    RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    private val appointments: MutableList<Appointment> = mutableListOf()

    fun addItems(newAppointments: List<Appointment>) {
        appointments.clear()
        appointments.addAll(newAppointments)
        notifyDataSetChanged()
    }

    fun enableSwipeActions(recyclerView: RecyclerView) {
        val swipeCallback = object : SwipeActionsCallback(context) {
            override fun canSwipe(position: Int): Boolean = true

            override fun onItemSwipedToDelete(position: Int) {
                adapterListener.onAppointmentDeleted(appointments[position])
                notifyItemRemoved(position)
            }

            override fun onItemSwipedToUpdate(position: Int) {
                adapterListener.onAppointmentUpdated(appointments[position])
                notifyItemChanged(position)
            }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.apply {
            bind(appointment)
            itemView.setOnClickListener { adapterListener.onAppointmentClicked(appointment, MainActivity()) }
        }
    }

    override fun getItemCount(): Int = appointments.size

    inner class AppointmentViewHolder(private val itemBinding: ItemAppointmentBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(appointment: Appointment) {
            itemBinding.apply {
                txtName.text = appointment.appointmentName
                txtLocation.text = appointment.appointmentLocation
                txtDate.text = appointment.appointmentDate
                txtTime.text = appointment.appointmentTime
            }
        }
    }
}

abstract class SwipeActionsCallback(var context: Context) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {

    private val deleteIcon =
        ContextCompat.getDrawable(context, R.drawable.img_delete)
    var deleteDrawableBackground = ColorDrawable(context.getColor(R.color.delete))

    private val updateIcon =
        ContextCompat.getDrawable(context, R.drawable.img_edit)
    var updateDrawableBackground = ColorDrawable(context.getColor(R.color.update))

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.RIGHT) {
            onItemSwipedToDelete(position)
        } else {
            onItemSwipedToUpdate(position)
        }
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val position = viewHolder.adapterPosition
        return if (canSwipe(position)) {
            super.getSwipeDirs(recyclerView, viewHolder)
        } else {
            0
        }
    }

    override fun onChildDrawOver(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            val iconMargin = (itemView.height - deleteIcon!!.intrinsicHeight) / 2
            val iconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
            val iconBottom = iconTop + deleteIcon.intrinsicHeight

            if (dX > 0) { //swiping to the right (delete)
                setupSwipeToRight(itemView, iconMargin, deleteIcon, dX, iconTop, iconBottom, canvas)
            } else if (dX < 0) { // Swiping to the left (update)
                setupSwipeToLeft(itemView, iconMargin, dX, updateIcon!!, iconTop, iconBottom, canvas)
            }
        }

        super.onChildDrawOver(
            canvas,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    private fun setupSwipeToLeft(
        itemView: View,
        iconMargin: Int,
        dX: Float,
        updateIcon: Drawable,
        iconTop: Int,
        iconBottom: Int,
        canvas: Canvas
    ) {
        val iconLeft = itemView.right - iconMargin - updateIcon.intrinsicWidth
        val iconRight = itemView.right - iconMargin
        updateDrawableBackground.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        updateIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        canvas.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        updateDrawableBackground.draw(canvas)
        updateIcon.draw(canvas)
    }

    private fun setupSwipeToRight(
        itemView: View,
        iconMargin: Int,
        deleteIcon: Drawable,
        dX: Float,
        iconTop: Int,
        iconBottom: Int,
        canvas: Canvas
    ) {
        val iconLeft = itemView.left + iconMargin
        val iconRight = itemView.left + iconMargin + deleteIcon.intrinsicWidth

        deleteDrawableBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        canvas.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
        deleteDrawableBackground.draw(canvas)
        deleteIcon.draw(canvas)
    }

    abstract fun canSwipe(position: Int): Boolean
    abstract fun onItemSwipedToDelete(position: Int)
    abstract fun onItemSwipedToUpdate(position: Int)
}