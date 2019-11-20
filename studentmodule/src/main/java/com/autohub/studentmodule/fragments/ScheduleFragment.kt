package com.autohub.studentmodule.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.autohub.skln.fragment.BaseFragment
import com.autohub.studentmodule.R
import com.autohub.studentmodule.adaptors.ScheduleCalenderAdapter
import com.autohub.studentmodule.adaptors.SchedulesAdaptor
import com.autohub.studentmodule.databinding.FragmentScheduleBinding
import com.autohub.studentmodule.models.BatchesModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Vt Netzwelt
 */
class ScheduleFragment : BaseFragment() {
    private lateinit var mBinding: FragmentScheduleBinding
    private lateinit var adaptor: SchedulesAdaptor
    private lateinit var scheduleData: MutableList<BatchesModel>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentScheduleBinding.bind(view)
        val dates = getDates("01-01-" + Calendar.getInstance().get(Calendar.YEAR),
                "31-12-" + Calendar.getInstance().get(Calendar.YEAR))
        initializeCalendarView(dates)
        scheduleData = ArrayList()
        setUpSeekBar(dates)

        mBinding.schedulerecycleview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adaptor = SchedulesAdaptor(requireContext())
        mBinding.schedulerecycleview.adapter = adaptor
        fetchBatches()
    }

    private fun fetchBatches() {
        firebaseStore.collection(getString(R.string.db_root_batches)).whereArrayContains("enrolledStudentsId", firebaseAuth.currentUser!!.uid)
                .get().addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        scheduleData.clear()
                        for (document in task.result!!) {
                            val batchesModel = document.toObject(com.autohub.studentmodule.models.BatchesModel::class.java)

                            try {
                                var endTime = uTCToLocal("EEE MMM dd HH:mm:ss z YYYY",
                                        "EEE, d MMM yyyy HH:mm:ss z", batchesModel.timing.endTime!!.toDate().toString()
                                )
                                var startTime = uTCToLocal("EEE MMM dd HH:mm:ss z YYYY",
                                        "EEE, d MMM yyyy HH:mm:ss z", batchesModel.timing.startTime!!.toDate().toString()
                                ).toString()
                                batchesModel.batchTiming =
                                        startTime + " - " + endTime
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                            scheduleData.add(batchesModel)
                        }

                        if (scheduleData.size > 0) {
                            mBinding.rrempty.visibility = View.GONE
                        } else {
                            mBinding.rrempty.visibility = View.VISIBLE
                        }


                        adaptor.setData(scheduleData)

                    }

                }
                .addOnFailureListener { e ->
                    showSnackError(e.message)
                }
    }

    private fun setUpSeekBar(dates: List<String>) {

        mBinding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                when {
                    i < 10 -> mBinding.calendarView.scrollToPosition(0)
                    i > 90 -> mBinding.calendarView.scrollToPosition(dates.size - 1)
                    (i * 3) < (dates.size - 1) -> mBinding.calendarView.scrollToPosition(i * 3)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })


    }


    private fun initializeCalendarView(dates: List<String>) {
        mBinding.calendarView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        val adapter = ScheduleCalenderAdapter(dates)
        mBinding.calendarView.adapter = adapter
    }


    private fun getDates(dateString1: String, dateString2: String): List<String> {
        val dates = ArrayList<String>()
        val df1 = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

        val format = SimpleDateFormat("MMM,dd,EEE", Locale.ENGLISH)

        lateinit var date1: Date
        lateinit var date2: Date

        try {
            date1 = df1.parse(dateString1)!!
            date2 = df1.parse(dateString2)!!
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val cal1 = Calendar.getInstance()
        cal1.time = date1


        val cal2 = Calendar.getInstance()
        cal2.time = date2

        while (!cal1.after(cal2)) {
            dates.add(format.format(cal1.time))
            cal1.add(Calendar.DATE, 1)
        }
        return dates
    }

    fun uTCToLocal(dateFormatInPut: String, dateFomratOutPut: String, datesToConvert: String): String? {


        val dateToReturn = datesToConvert

        val sdf = SimpleDateFormat(dateFormatInPut)

        var gmt: Date? = null

        val sdfOutPutToSend = SimpleDateFormat(dateFomratOutPut)
        sdfOutPutToSend.timeZone = TimeZone.getDefault()
        try {
            gmt = sdf.parse(datesToConvert)

        } catch (e: ParseException) {
            e.printStackTrace()
        }


        val originalFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z YYYY", Locale.ENGLISH)
        val targetFormat = SimpleDateFormat("h:mm a")
        val date = originalFormat.parse(gmt.toString())
        val formattedDate = targetFormat.format(date)






        return formattedDate
    }

}
