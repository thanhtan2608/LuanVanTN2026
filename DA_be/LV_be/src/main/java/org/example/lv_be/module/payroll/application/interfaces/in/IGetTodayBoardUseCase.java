package org.example.lv_be.module.payroll.application.interfaces.in;
import org.example.lv_be.module.payroll.application.dto.response.TodayAttendanceBoardResponse;
import java.util.List;

public interface IGetTodayBoardUseCase {
    List<TodayAttendanceBoardResponse> execute();
}