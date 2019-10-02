<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="scrollableContainer blockFlex fullSize">
	<div class="totallyCenteredContainer tileContainer">

		<div class="borderedTile">
			<div class="padded">

				<table class="table">
					<thead>
						<tr>
							<th>User</th>
							<th>Text</th>
							<th>Date</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ requestScope.improvementSuggestions }"
							var="improvementSuggestion">
							<tr>
								<td>
									<c:out value="${ improvementSuggestion.user.nickname }" />
								</td>
								<td>
									<span class="keepLineBreak readabilityToImprove"><c:out value="${ improvementSuggestion.text }" /></span>
								</td>
								<td>
									<span class="dateToFormat" data-format="1"> <c:out
											value="${ improvementSuggestion.instant }" />
									</span>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
