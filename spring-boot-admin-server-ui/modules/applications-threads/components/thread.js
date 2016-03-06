/*
 * Copyright 2014 the original author or authors.
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
 */
'use strict';

module.exports = {
        bindings : {
            thread : '<thread',
        },
        controller : function() {
            var ctrl = this;
            ctrl.getStateClass = function() {
                switch (ctrl.thread.threadState) {
                    case 'NEW':
                    case 'TERMINATED':
                        return 'label-info';
                    case 'RUNNABLE':
                        return 'label-success';
                    case 'BLOCKED':
                        return 'label-important';
                    case 'TIMED_WAITING':
                    case 'WAITING':
                        return 'label-warning';
                    default:
                        return 'label-info';
                }
            };
        },
        template : '<sba-accordion-group> '
                + '	<sba-accordion-heading> '
                + '		<small class="muted" ng-bind="$ctrl.thread.threadId"></small> {{$ctrl.thread.threadName}} <span class="pull-right label" ng-class="$ctrl.getStateClass()" ng-bind="$ctrl.thread.threadState"></span> <span class="label label-warning" ng-show="$ctrl.thread.suspended">suspended</span> '
                + '	</sba-accordion-heading> '
                + '	<sba-accordion-body> '
                + '		<div class="container"> '
                + '			<div class="row" > '
                + '				<table class="span6"> '
                + '					<col style="min-width: 10em;"/> '
                + '					<tr><td>Blocked count</td><td ng-bind="$ctrl.thread.blockedCount"></td></tr> '
                + '					<tr><td>Blocked time</td><td ng-bind="$ctrl.thread.blockedTime"></td></tr> '
                + '					<tr><td>Waited count</td><td ng-bind="$ctrl.thread.waitedCount"></td></tr> '
                + '					<tr><td>Waited time</td><td ng-bind="$ctrl.thread.waitedTime"></td></tr> '
                + '				</table> '
                + '				<table class="span6"> '
                + '					<col style="min-width: 10em;"/> '
                + '					<tr><td>Lock name</td><td style="word-break: break-word;" ng-bind="$ctrl.thread.lockName"></td></tr> '
                + '					<tr><td>Lock owner id</td><td ng-bind="$ctrl.thread.lockOwnerId"></td></tr> '
                + '					<tr><td>Lock owner name</td><td style="word-break: break-word;" ng-bind="$ctrl.thread.lockOwnerName"></td></tr> '
                + '				</table> '
                + '			</div> '
                + '		</div> '
                + '		<pre style="overflow: auto; max-height: 20em" ng-show="$ctrl.thread.stackTrace.length > 0"><span ng-repeat="el in $ctrl.thread.stackTrace">{{el.className}}.{{el.methodName}}({{el.fileName}}:{{el.lineNumber}}) <span class="label" ng-show="el.nativeMethod">native</span><br/></span></pre> '
                + '	</sba-accordion-body> '
                + '</sba-accordion-group> '
    };
